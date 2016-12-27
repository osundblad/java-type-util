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
package se.eris.jtype.type;

import org.jetbrains.annotations.Nullable;


/**
 * Two things of which at least one must absent (null).
 *
 * @param <T> the first type
 * @param <U> the second type
 *
 * @see PairWrapper
 */
public abstract class MaxOneOfWrapper<T, U> extends OneOfBaseWrapper<T, U> {

    protected MaxOneOfWrapper(@Nullable final T first, @Nullable final U second) {
        super(second, first);
        if (this.first.isPresent() && this.second.isPresent()) {
            throw new RuntimeException("Both values cannot be present");
        }
    }

}
