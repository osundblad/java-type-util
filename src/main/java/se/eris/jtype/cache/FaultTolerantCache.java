/*
 *    Copyright 2016 Olle Sundblad - olle@eris.se
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package se.eris.jtype.cache;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class FaultTolerantCache<K, V> {

    private final Map<K, Dated<V>> cache = new HashMap<>();
    private final Map<K, LocalDateTime> locks = new ConcurrentHashMap<>();

    private final Function<K, Optional<V>> source;
    private final CacheParameters<K> cacheParameters;
    private final Supplier<LocalDateTime> timeSupplier;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static <K, V> FaultTolerantCache<K, V> of(final Function<K, Optional<V>> source, final CacheParameters<K> cacheParameters) {
        return of(source, cacheParameters, LocalDateTime::now);
    }

    public static <K, V> FaultTolerantCache<K, V> of(final Function<K, Optional<V>> source, final CacheParameters<K> cacheParameters, final Supplier<LocalDateTime> timeSupplier) {
        return new FaultTolerantCache<>(source, cacheParameters, timeSupplier);
    }

    private FaultTolerantCache(final Function<K, Optional<V>> source, final CacheParameters<K> cacheParameters, final Supplier<LocalDateTime> timeSupplier) {
        this.source = source;
        this.cacheParameters = cacheParameters;
        this.timeSupplier = timeSupplier;
    }

    public Optional<V> get(final K key) {
        final Optional<Dated<V>> opDated = Optional.ofNullable(cache.get(key));
        if (!opDated.isPresent()) {
            return syncedFetch(key);
        }
        final Dated<V> dated = opDated.get();
        final LocalDateTime now = timeSupplier.get();

        if (now.isBefore(getEarliestRefetchTime(dated.getDateTime()))) {
            return Optional.of(dated.getSubject());
        }
        if (now.isBefore(getSyncedRefetchTime(dated.getDateTime()))) {
            asyncFetch(key);
            return Optional.of(dated.getSubject());
        }
        return syncedFetch(key, dated.getSubject());
    }


    private void asyncFetch(final K key) {
        if (lock(key)) {
            CompletableFuture
                    .supplyAsync(() -> source.apply(key), executorService)
                    .handle((result, e) -> {
                        try {
                            if (result.isPresent()) {
                                return result;
                            }
                            cacheParameters.getSupplierFailedAction().ifPresent(consumer -> consumer.accept(key, e));
                            throw new SupplierFailedException(getSupplierFailedMessage(key), e);
                        } finally {
                            unlock(key);
                        }
                    })
                    .thenAccept(v -> cache.put(key, Dated.of(timeSupplier.get(), v.get())));
        }
    }

    private synchronized boolean lock(final K key) {
        if (locks.containsKey(key)) {
            return false;
        }
        locks.put(key, timeSupplier.get());
        return true;
    }

    private void unlock(final K key) {
        locks.remove(key);
    }

    private Optional<V> syncedFetch(final K key, final V defaultValue) {
        try {
            return syncedFetch(key);
        } catch (final Exception e) {
            return Optional.of(defaultValue);
        }
    }

    private Optional<V> syncedFetch(final K key) {
        try {
            final Optional<V> fetched = source.apply(key);
            fetched.ifPresent(v -> put(key, v));
            return fetched;
        } catch (final RuntimeException e) {
            cacheParameters.getSupplierFailedAction().ifPresent(throwableConsumer -> throwableConsumer.accept(key, e));
            throw new SupplierFailedException(getSupplierFailedMessage(key), e);
        }
    }

    private ChronoLocalDateTime getSyncedRefetchTime(final ChronoLocalDateTime dateTime) {
        return dateTime.plus(cacheParameters.getRefetchSyncPeriod());
    }

    private ChronoLocalDateTime getEarliestRefetchTime(final ChronoLocalDateTime dateTime) {
        return dateTime.plus(cacheParameters.getRefetchAsyncPeriod());
    }

    private String getSupplierFailedMessage(final K key) {
        return "Source " + source + " failed to get key " + key;
    }

    public Optional<V> getIfPresent(final K key) {
        return Optional.ofNullable(cache.get(key)).map(Dated::getSubject);
    }

    public void put(final K key, final V value) {
        cache.put(key, Dated.of(timeSupplier.get(), value));
    }

    public Map<K, V> getPresent(final Collection<K> keys) {
        return cache.entrySet().stream()
                .filter(e -> keys.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getSubject()));
    }

    public Map<K, V> getPresentFresh(final Collection<K> keys) {
        final ChronoLocalDateTime fetchedAfter = timeSupplier.get().minus(cacheParameters.getRefetchSyncPeriod());
        return cache.entrySet().stream()
                .filter(e -> keys.contains(e.getKey()))
                .filter(e -> e.getValue().getDateTime().isAfter(fetchedAfter))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getSubject()));
    }

}
