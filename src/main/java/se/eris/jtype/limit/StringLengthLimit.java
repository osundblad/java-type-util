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
package se.eris.jtype.limit;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public class StringLengthLimit implements StringLimit {

    public static final int LONGEST_STRING_TO_PRESENT = 80;

    @NotNull
    public static StringLengthLimit zeroTo(final int max) {
        return of(0, max);
    }

    @NotNull
    public static StringLengthLimit oneTo(final int max) {
        return of(1, max);
    }

    @NotNull
    public static StringLengthLimit atLeast(final int min) {
        return of(min, Integer.MAX_VALUE);
    }

    @NotNull
    public static StringLengthLimit exactly(final int length) {
        return of(length, length);
    }

    @NotNull
    public static StringLengthLimit of(final int min, final int max) {
        return new StringLengthLimit(min, max);
    }

    private final int min;
    private final int max;

    private StringLengthLimit(final int min, final int max) {
        if (min < 0) {
            throw new IllegalArgumentException("Min " + min + " less than zero");
        }
        if (min > max) {
            throw new IllegalArgumentException("Min " + min + " greater than max '" + max + "'");
        }
        this.min = min;
        this.max = max;
    }

    @Override
    @NotNull
    public Optional<ValidationError> validate(@NotNull final String s) {
        final int length = s.length();
        if (length < min) {
            return Optional.of(ValidationError.of("Length violation: " + quote(shorten(s)) + " is shorter (" + length + ") than the minimum length of " + min + "."));
        }
        if (length > max) {
            return Optional.of(ValidationError.of("Length violation: " + quote(shorten(s)) + " is longer (" + length + ") than the maximum length of " + max + "."));
        }
        return Optional.empty();
    }

    @NotNull
    private String quote(@NotNull final String s) {
        return '\'' + s + '\'';
    }

    @NotNull
    private String shorten(@NotNull final String s) {
        return (s.length() > LONGEST_STRING_TO_PRESENT) ? truncate(s) : s;
    }

    @NotNull
    private String truncate(@NotNull final String s) {
        return s.substring(0, LONGEST_STRING_TO_PRESENT) + "...' (truncated at " + LONGEST_STRING_TO_PRESENT + " characters)";
    }

}
