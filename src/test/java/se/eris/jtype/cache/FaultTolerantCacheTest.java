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
import java.util.function.Function;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FaultTolerantCacheTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final CacheParameters<String> CACHE_PARAMETERS = CacheParameters.of(Duration.ofMinutes(20), Duration.ofMinutes(5));

    private final Function<String, Optional<Integer>> source =
            (s) -> {
                if (s.startsWith("E")) {
                    throw new RuntimeException("Supplier Error");
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
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS, new TimeSupplier());

        exception.expect(RuntimeException.class);
        exception.expectMessage("Supplier Error");
        cache.get("ERROR");
    }

    @Test(timeout = 1000)
    public void get_notOutDated_returnValue() {
        final TimeSupplier timeSupplier = new TimeSupplier();
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS, timeSupplier);
        cache.put("S20000:AB", 1);

        timeSupplier.step(Duration.ofMinutes(1));
        assertThat(cache.get("S20000:AB"), is(Optional.of(1)));
    }

    @Test(timeout = 10000)
    public void get_afterRefetchAsyncPeriod_returnCachedValue() {
        final TimeSupplier timeSupplier = new TimeSupplier();
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS, timeSupplier);
        final String key = "S200:ABC";
        cache.put(key, 1);

        timeSupplier.step(Duration.ofMinutes(10));
        assertThat(cache.get(key), is(Optional.of(1)));
        //noinspection StatementWithEmptyBody,OptionalGetWithoutIsPresent
        while (cache.get(key).get() == 1) {
            Thread.yield();
        }
        assertThat(cache.get(key), is(Optional.of(8)));
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

        assertThat(cache.getIfPresent("A"), is(Optional.empty()));
    }

    @Test
    public void getPresent_mixed_returnPresent() {
        final FaultTolerantCache<String, Integer> cache = FaultTolerantCache.of(source, CACHE_PARAMETERS);
        cache.put("A", 1);
        cache.put("B", 2);

        final Map<String, Integer> present = cache.getPresent(new HashSet<>(Arrays.asList("A", "B", "C")));
        assertThat(present.size(), is(2));
    }

    private static class TimeSupplier implements Supplier<LocalDateTime> {
        private static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;
        private LocalDateTime time = LocalDateTime.ofEpochSecond(0, 0, ZONE_OFFSET);

        @Override
        public LocalDateTime get() {
            return time;
        }

        public LocalDateTime step(@NotNull final TemporalAmount temporalAmount) {
            time = time.plus(temporalAmount);
            return time;
        }
    }

}
