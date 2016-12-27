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

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SingleFetchTrackerTest {
    @Test
    public void mark() {
        final SingleFetchTracker<String> fetchTracker = new SingleFetchTracker<>(Duration.ofSeconds(10));
        final LocalDateTime now = LocalDateTime.now();

        assertThat(fetchTracker.mark("key", now), is(true));
        assertThat(fetchTracker.mark("key", now), is(false));
        assertThat(fetchTracker.mark("key", now.plusSeconds(10)), is(true));
    }

    @Test
    public void unmark() {
        final SingleFetchTracker<String> fetchTracker = new SingleFetchTracker<>(Duration.ofSeconds(10));

        final LocalDateTime now = LocalDateTime.now();
        fetchTracker.mark("key", now);

        assertThat(fetchTracker.unmark("key"), is(true));
        assertThat(fetchTracker.unmark("key"), is(false));
    }

}