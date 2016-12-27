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

import se.eris.jtype.cache.dated.Dated;
import se.eris.jtype.cache.dated.FetchedAt;
import se.eris.jtype.cache.dated.NextFetchTime;

import java.time.LocalDateTime;
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

    private final Map<K, Dated<V>> cache = new ConcurrentHashMap<>();
    private final Map<K, LocalDateTime> locks = new HashMap<>();

    private final Function<K, Optional<V>> source;
    private final CacheParameters<K> cacheParameters;
    private final Supplier<LocalDateTime> timeSupplier;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

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
            return syncFetch(key);
        }
        final Dated<V> dated = opDated.get();
        final LocalDateTime now = timeSupplier.get();

        if (dated.isFresh(now)) {
            return Optional.of(dated.getSubject());
        }
        if (now.isBefore(nextSyncFetch(dated))) {
            asyncFetch(key);
            return Optional.of(dated.getSubject());
        }
        return syncFetch(key, dated.getSubject());
    }

    @SuppressWarnings("TypeMayBeWeakened")
    private LocalDateTime nextSyncFetch(final Dated dated) {
        return dated.getFetchTime().plus(cacheParameters.getSyncFetchPeriod());
    }

    private void asyncFetch(final K key) {
        if (lock(key)) {
            CompletableFuture
                    .supplyAsync(() -> syncFetch(key), executorService)
                    .handle((Optional<V> v, Throwable e) -> unlock(key));
        }
    }

    private synchronized boolean lock(final K key) {
        if (locks.containsKey(key)) {
            return false;
        }
        locks.put(key, timeSupplier.get());
        return true;

    }

    private boolean unlock(final K key) {
        return locks.remove(key) != null;
    }

    private Optional<V> syncFetch(final K key, final V defaultValue) {
        try {
            return syncFetch(key);
        } catch (final Exception e) {
            return Optional.of(defaultValue);
        }
    }

    // fix only one active call per key
    private Optional<V> syncFetch(final K key) {
        try {
            final Optional<V> fetched = source.apply(key);
            fetched.ifPresent(v -> put(key, v));
            return fetched;
        } catch (final RuntimeException e) {
            updateNextFetchTime(key);
            cacheParameters.getSupplierFailedAction().ifPresent(throwableConsumer -> throwableConsumer.accept(key, e));
            throw new SupplierFailedException(getSupplierFailedMessage(key), e);
        }
    }

    private void updateNextFetchTime(final K key) {
        final Dated<V> dated = cache.get(key);
        if (dated != null) {
            cache.put(key, createFailedDated(dated.getSubject(), timeSupplier.get()));
        }
    }

    private Dated<V> createFailedDated(final V value, final LocalDateTime now) {
        return Dated.sucessful(value, FetchedAt.of(now), NextFetchTime.of(now.plus(cacheParameters.getAsyncFetchPeriod())));
    }

    private String getSupplierFailedMessage(final K key) {
        return "Source failed to get key " + key;
    }

    @SuppressWarnings("WeakerAccess")
    public Optional<V> getIfPresent(final K key) {
        return Optional.ofNullable(cache.get(key)).map(Dated::getSubject);
    }

    @SuppressWarnings("WeakerAccess")
    public void put(final K key, final V value) {
        cache.put(key, createSuccessfulDated(value, timeSupplier.get()));
    }

    private Dated<V> createSuccessfulDated(final V value, final LocalDateTime now) {
        return Dated.sucessful(value, FetchedAt.of(now), NextFetchTime.of(now.plus(cacheParameters.getAsyncFetchPeriod())));
    }

    @SuppressWarnings("WeakerAccess")
    public Map<K, V> getPresent(final Collection<K> keys) {
        return cache.entrySet().stream()
                .filter(e -> keys.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getSubject()));
    }

    @SuppressWarnings("WeakerAccess")
    public Map<K, V> getPresentFresh(final Collection<K> keys) {
        final LocalDateTime now = timeSupplier.get();
        return cache.entrySet().stream()
                .filter(e -> keys.contains(e.getKey()))
                .filter(e -> e.getValue().isFresh(now))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getSubject()));
    }

}
