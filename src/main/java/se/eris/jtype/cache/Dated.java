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
import se.eris.jtype.type.DyadWrapper;

import java.time.LocalDateTime;

public final class Dated<S> extends DyadWrapper<LocalDateTime, S> {

    @NotNull
    public static <S> Dated<S> of(@NotNull final LocalDateTime first, @NotNull final S second) {
        return new Dated<>(first, second);
    }

    private Dated(@NotNull final LocalDateTime first, @NotNull final S second) {
        super(first, second);
    }

    @NotNull
    public LocalDateTime getDateTime() {
        return rawFirst();
    }

    @NotNull
    public S getSubject() {
        return rawSecond();
    }
}
