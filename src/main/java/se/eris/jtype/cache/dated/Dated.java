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

import java.time.LocalDateTime;

public final class Dated<S> extends DyadWrapper<S, FetchTimes> {

    public static <S> Dated<S> sucessful(final S subject, final FetchedAt fetchedAt, final NextFetchTime nextFetchTime) {
        return new Dated<>(subject, FetchTimes.successful(fetchedAt, nextFetchTime));
    }

    private Dated(final S subject, final FetchTimes fetchTimes) {
        super(subject, fetchTimes);
    }

    public FetchedAt getFetchTime() {
        return getFetchTimes().getFetchedAt();
    }

    public S getSubject() {
        return rawFirst();
    }

    public FetchTimes getFetchTimes() {
        return rawSecond();
    }

    private NextFetchTime getNextFetchTime() {
        return rawSecond().getNextFetchTime();
    }

    public boolean isFresh(final LocalDateTime now) {
        return now.isBefore(getNextFetchTime().asLocalDateTime());
    }
}