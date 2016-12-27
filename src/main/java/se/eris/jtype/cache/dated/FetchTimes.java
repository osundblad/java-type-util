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
package se.eris.jtype.cache.dated;

import se.eris.jtype.type.DyadWrapper;

public class FetchTimes extends DyadWrapper<FetchedAt, NextFetchTime>{

    public static FetchTimes successful(final FetchedAt fetchedAt, final NextFetchTime nextFetchTime) {
        return new FetchTimes(fetchedAt, nextFetchTime);
    }

    public FetchTimes failed(final NextFetchTime nextFetchTime) {
        return successful(rawFirst(), nextFetchTime);
    }

    private FetchTimes(final FetchedAt first, final NextFetchTime second) {
        super(first, second);
    }

    public FetchedAt getFetchedAt() {
        return rawFirst();
    }

    public NextFetchTime getNextFetchTime() {
        return rawSecond();
    }

}
