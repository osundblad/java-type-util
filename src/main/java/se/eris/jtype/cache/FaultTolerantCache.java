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

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class FaultTolerantCache<K, V> {

    @NotNull
    private final Map<K, Dated<V>> cache = new ConcurrentHashMap<>();

    @NotNull
    private final Function<K, Optional<V>> source;
    @NotNull
    private final CacheParameters<K> cacheParameters;
    @NotNull
    private final Supplier<LocalDateTime> timeSupplier;

    @NotNull
    public static <K, V> FaultTolerantCache<K, V> of(@NotNull final Function<K, Optional<V>> source, @NotNull final CacheParameters<K> cacheParameters) {
        return of(source, cacheParameters, LocalDateTime::now);
    }

    @NotNull
    public static <K, V> FaultTolerantCache<K, V> of(@NotNull final Function<K, Optional<V>> source, @NotNull final CacheParameters<K> cacheParameters, @NotNull final Supplier<LocalDateTime> timeSupplier) {
        return new FaultTolerantCache<>(source, cacheParameters, timeSupplier);
    }

    private FaultTolerantCache(@NotNull final Function<K, Optional<V>> source, @NotNull final CacheParameters<K> cacheParameters, @NotNull final Supplier<LocalDateTime> timeSupplier) {
        this.source = source;
        this.cacheParameters = cacheParameters;
        this.timeSupplier = timeSupplier;
    }

    @NotNull
    public Optional<V> get(@NotNull final K key) {
        final Optional<Dated<V>> opDated = Optional.ofNullable(cache.get(key));
        if (!opDated.isPresent()) {
            return fetch(key);
        }
        final Dated<V> dated = opDated.get();
        final LocalDateTime now = timeSupplier.get();

        if (now.isBefore(getEarliestRefetchTime(dated))) {
            return Optional.of(dated.getSubject());
        }
        if (now.isBefore(getSyncedRefetchTime(dated))) {
            asyncFetch(key);
            return Optional.of(dated.getSubject());
        }
        return syncedFetch(key, dated);
    }

    @NotNull
    private Optional<V> syncedFetch(@NotNull final K key, final Dated<V> dated) {
        try {
            return fetch(key);
        } catch (final RuntimeException e) {
            cacheParameters.getSupplierFailedAction().ifPresent(throwableConsumer -> throwableConsumer.accept(key, e));
            return Optional.of(dated.getSubject());
        }
    }

    @NotNull
    private ChronoLocalDateTime getSyncedRefetchTime(final Dated<V> dated) {
        return dated.getDateTime().plus(cacheParameters.getSyncedRefetchPeriod());
    }

    @NotNull
    private ChronoLocalDateTime getEarliestRefetchTime(@NotNull final Dated<V> dated) {
        return dated.getDateTime().plus(cacheParameters.getAsyncRefetchPeriod());
    }

    @NotNull
    private String getFetchFaileMessage(@NotNull final K key) {
        return "Source " + source + " failed to get key " + key;
    }

    private void asyncFetch(@NotNull final K key) {
        final CompletableFuture<Optional<V>> future = CompletableFuture.supplyAsync(() -> source.apply(key));
        future.handle((result, e) -> {
            if (result.isPresent()) {
                return result;
            }
            throw new RuntimeException(getFetchFaileMessage(key), e);
        }).thenAccept(v -> cache.put(key, Dated.of(timeSupplier.get(), v.get())));
    }


    @NotNull
    private Optional<V> fetch(@NotNull final K key) {
        final Optional<V> fetched = source.apply(key);
        fetched.ifPresent(v -> put(key, v));
        return fetched;
    }

    @NotNull
    public Optional<V> getIfPresent(@NotNull final K key) {
        return Optional.ofNullable(cache.get(key)).map(Dated::getSubject);
    }

    public void put(@NotNull final K key, @NotNull final V value) {
        cache.put(key, Dated.of(timeSupplier.get(), value));
    }

    @NotNull
    public Map<K, V> getPresent(@NotNull final Collection<K> keys) {
        return cache.entrySet().stream()
                .filter(e -> keys.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getSubject()));
    }

}
