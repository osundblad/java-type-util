/*
 *    Copyright 2016 Olle Sundblad
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
package se.eris.util.limit;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class IntegerRangeLimit implements Limit<Integer> {

    @NotNull
    public static IntegerRangeLimit zeroTo(final int max) {
        return of(0, max);
    }

    @NotNull
    public static IntegerRangeLimit of(final int min, final int max) {
        return new IntegerRangeLimit(min, max);
    }

    private final int min;
    private final int max;

    private IntegerRangeLimit(final int min, final int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min " + min + " greater than max '" + max + "'");
        }
        this.min = min;
        this.max = max;
    }

    @Override
    @NotNull
    public Optional<ValidationError> validate(@NotNull final Integer i) {
        if (i < min) {
            return Optional.of(ValidationError.of(i + " is less than min " + min));
        }
        if (i > max) {
            return Optional.of(ValidationError.of(i + " is greater than max " + max));
        }
        return Optional.empty();
    }

}
