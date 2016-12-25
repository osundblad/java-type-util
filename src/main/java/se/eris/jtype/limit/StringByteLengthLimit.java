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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class StringByteLengthLimit implements StringLimit {


    public static StringByteLengthLimit zeroTo(final int max) {
        return of(0, max);
    }


    public static StringByteLengthLimit of(final int min, final int max) {
        return new StringByteLengthLimit(min, max, Optional.empty());
    }


    public static StringByteLengthLimit of(final int min, final int max,  final Charset charset) {
        return new StringByteLengthLimit(min, max, Optional.of(charset));
    }

    private final int min;
    private final int max;
    private final Charset charset;

    private StringByteLengthLimit(final int min, final int max,  final Optional<Charset> charset) {
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

    @Override

    public Optional<ValidationError> validate( final String s) {
        final int length = getByteLength(s);
        if (length < min) {
            return Optional.of(ValidationError.of("Byte length of " + s + " is less than min " + min));
        }
        if (length > max) {
            return Optional.of(ValidationError.of("Byte length of " + s + " is greater than max " + max));
        }
        return Optional.empty();
    }

    private int getByteLength( final String s) {
        return s.getBytes(charset).length;
    }

}
