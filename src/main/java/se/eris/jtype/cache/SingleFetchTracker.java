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
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.Map;

class SingleFetchTracker<K> {

    private final Map<K, LocalDateTime> currentlyFetching = new HashMap<>();
    private final TemporalAmount fetchTimeoutPeriod;

    SingleFetchTracker(final TemporalAmount fetchTimeoutPeriod) {
        this.fetchTimeoutPeriod = fetchTimeoutPeriod;
    }

    synchronized boolean mark(final K key, final LocalDateTime now) {
        final LocalDateTime markedAt = currentlyFetching.get(key);
        if (markedAt != null) {
            if (now.isBefore(markedAt.plus(this.fetchTimeoutPeriod))) {
                return false;
            }
        }
        currentlyFetching.put(key, now);
        return true;
    }

    boolean unmark(final K key) {
        return currentlyFetching.remove(key) != null;
    }

}
