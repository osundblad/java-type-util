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

import se.eris.jtype.type.BasicWrapper;

import java.time.LocalDateTime;

public class FetchedAt extends BasicWrapper<LocalDateTime> {

    public static FetchedAt of(final LocalDateTime fetchedAt) {
        return new FetchedAt(fetchedAt);
    }

    private FetchedAt(final LocalDateTime item) {
        super(item);
    }

    public LocalDateTime asLocalDateTime() {
        return raw();
    }

}
