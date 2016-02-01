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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class StringByteLengthLimit implements StringLimit {

    @NotNull
    public static StringByteLengthLimit zeroTo(final int max) {
        return of(0, max);
    }

    @NotNull
    public static StringByteLengthLimit of(final int min, final int max) {
        return new StringByteLengthLimit(min, max, Optional.empty());
    }

    @NotNull
    public static StringByteLengthLimit of(final int min, final int max, @NotNull final Charset charset) {
        return new StringByteLengthLimit(min, max, Optional.of(charset));
    }

    private final int min;
    private final int max;
    private final Charset charset;

    private StringByteLengthLimit(final int min, final int max, @NotNull final Optional<Charset> charset) {
        this.charset = charset.orElse(StandardCharsets.UTF_8);
        if (min < 0) {
            throw new IllegalArgumentException("Min " + min + " less than zero");
        }
        if (min > max) {
            throw new IllegalArgumentException("Min " + min + " greater than max '" + max + "'");
        }
        this.min = min;
        this.max = max;
    }

    @NotNull
    @Override
    public ValidationMessages validate(@NotNull final String s) {
        final int length = getByteLength(s);
        if (length < min) {
            return ValidationMessages.of("Byte length of " + s + " is less than min " + min);
        }
        if (length > max) {
            return ValidationMessages.of("Byte length of " + s + " is greater than max " + max);
        }
        return ValidationMessages.empty();
    }

    private int getByteLength(@NotNull final String s) {
        return s.getBytes(charset).length;
    }

}
