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

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FaultTolerantCacheTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final Duration REFETCH_SYNC_PERIOD = Duration.ofMinutes(20);
    private static final Duration REFETCH_ASYNC_PERIOD = Duration.ofMinutes(5);

    private static final CacheParameters<String> CACHE_PARAMETERS = CacheParameters.Builder.<String>init()
            .asyncFetchPeriod(REFETCH_ASYNC_PERIOD)
            .syncFetchPeriod(REFETCH_SYNC_PERIOD)
            .build();

    private static final Function<String, Optional<Integer>> source =
            (s) -> {
                if (s.startsWith("E")) {
                    throw new IllegalArgumentException("Supplier Error");
                }
                if (s.matches("S[0-9]+:.*")) {
                    try {
                        Thread.sleep(Long.parseLong(s.substring(1, s.indexOf(":"))));
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Sleep failed", e);
                    }
                }
                return Optional.of(s.length());
            };

    @Test
    public void get_asyncFailingSourceWithCache_shouldReturnCachedValue() {
        final TimeSupplier timeSupplier = new TimeSupplier();
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS, timeSupplier);
        cache.put("ERROR", 1);

        timeSupplier.step(Duration.ofMinutes(10));
        assertThat(cache.get("ERROR"), is(Optional.of(1)));
    }

    @Test
    public void get_syncFailingSourceWithCache_shouldReturnCachedValue() {
        final TimeSupplier timeSupplier = new TimeSupplier();
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS, timeSupplier);
        cache.put("ERROR", 1);

        timeSupplier.step(Duration.ofHours(5));
        assertThat(cache.get("ERROR"), is(Optional.of(1)));
    }

    @Test
    public void get_failingSourceWithoutCache_shouldThrow() {
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS);

        exception.expect(SupplierFailedException.class);
        exception.expectCause(Matchers.any(IllegalArgumentException.class));
        exception.expectMessage("failed to get key");
        cache.get("ERROR");
    }

    @Test
    public void get_outDated_returnValue() {
        final TimeSupplier timeSupplier = new TimeSupplier();
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS, timeSupplier);
        cache.put("AB", 1);
        assertThat(cache.get("AB"), is(Optional.of(1)));

        timeSupplier.step(Duration.ofDays(10));
        assertThat(cache.get("AB"), is(Optional.of("AB".length())));
    }

    @Test(timeout = 1000)
    public void get_notOutDated_returnValue() {
        final TimeSupplier timeSupplier = new TimeSupplier();
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS, timeSupplier);
        cache.put("S20000:AB", 1);

        timeSupplier.step(Duration.ofMinutes(1));
        assertThat(cache.get("S20000:AB"), is(Optional.of(1)));
    }

    @Test(timeout = 1000)
    public void get_afterRefetchAsyncPeriod_returnCachedValue() {
        final TimeSupplier timeSupplier = new TimeSupplier();
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS, timeSupplier);
        final String key = "S200:ABC";

        cache.put(key, 1);
        timeSupplier.step(Duration.ofMinutes(10));

        System.out.println("Thread.activeCount() = " + Thread.activeCount());

        assertThat(cache.get(key), is(Optional.of(1)));
        while (cache.get(key).equals(Optional.of(1))) {
            try {
                System.out.println("Thread.activeCount() = " + Thread.activeCount());
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (final InterruptedException e) {
                // ignore
            }
        }
        assertThat(cache.get(key), is(Optional.of(8)));
    }

    @Test
    public void get_notExisisting_shouldFetchAndReturnValue() {
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS);

        assertThat(cache.get("A"), is(Optional.of(1)));
    }

    @Test
    public void getIfPresent_exisisting_returnValue() {
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS);
        cache.put("A", 1);

        assertThat(cache.getIfPresent("A"), is(Optional.of(1)));
    }

    @Test
    public void getIfPresent_nonExisisting_returnEmpty() {
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS);

        assertThat(cache.getIfPresent("B"), is(Optional.empty()));
    }

    @Test
    public void getPresent_mixedData_returnAllPresent() {
        final TimeSupplier timeSupplier = new TimeSupplier();
        final FaultTolerantCache<String, Integer> cache = setupCacheMixedData(timeSupplier);

        final Map<String, Integer> present = cache.getPresent(new HashSet<>(Arrays.asList("A", "B", "C")));
        assertThat(present.size(), is(2));
        assertThat(present.containsKey("A"), is(true));
        assertThat(present.containsKey("B"), is(true));
    }

    @Test
    public void getPresentFresh_mixedData_returnOnlyFresh() {
        final TimeSupplier timeSupplier = new TimeSupplier();
        final FaultTolerantCache<String, Integer> cache = setupCacheMixedData(timeSupplier);

        final Map<String, Integer> present = cache.getPresentFresh(new HashSet<>(Arrays.asList("A", "B", "C")));
        assertThat(present.size(), is(1));
        assertThat(present.containsKey("B"), is(true));
        assertThat(present.get("B"), is(2));
    }

    private FaultTolerantCache<String, Integer> setupCacheMixedData(final TimeSupplier timeSupplier) {
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS, timeSupplier);
        cache.put("A", 2);
        timeSupplier.step(REFETCH_SYNC_PERIOD);
        cache.put("B", 2);
        timeSupplier.step(REFETCH_ASYNC_PERIOD.plusMinutes(1));
        return cache;
    }

    private static class TimeSupplier implements Supplier<LocalDateTime> {
        private static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;
        private LocalDateTime time = LocalDateTime.ofEpochSecond(0, 0, ZONE_OFFSET);

        @Override
        public LocalDateTime get() {
            return time;
        }

        LocalDateTime step(final TemporalAmount temporalAmount) {
            time = time.plus(temporalAmount);
            return time;
        }
    }

}
