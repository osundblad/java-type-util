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

import java.math.BigDecimal;

public class BigDecimalRangeLimit implements Limit<BigDecimal> {

    @NotNull
    public static BigDecimalRangeLimit zeroTo(final BigDecimal max) {
        return of(BigDecimal.ZERO, max);
    }

    @NotNull
    public static BigDecimalRangeLimit of(final BigDecimal min, final BigDecimal max) {
        return new BigDecimalRangeLimit(min, max);
    }

    private final BigDecimal min;
    private final BigDecimal max;

    private BigDecimalRangeLimit(final BigDecimal min, final BigDecimal max) {
        if (min.compareTo(max) == 1) {
            throw new IllegalArgumentException("Min " + min + " greater than max '" + max + "'");
        }
        this.min = min;
        this.max = max;
    }

    @Override
    public @NotNull ValidationErrors validate(@NotNull final BigDecimal bigDecimal) {
        if (bigDecimal.compareTo(min) == -1) {
            return ValidationErrors.of(bigDecimal + " is less than min " + min);
        }
        if (bigDecimal.compareTo(max) == 1) {
            return ValidationErrors.of(bigDecimal + " is greater than max " + max);
        }
        return ValidationErrors.empty();
    }

}
