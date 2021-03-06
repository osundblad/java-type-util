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

import java.math.BigDecimal;
import java.util.Optional;

public class BigDecimalDecimalLimit implements Limit<BigDecimal> {

    public static Limit<BigDecimal> of(final int decimals) {
        return new BigDecimalDecimalLimit(decimals);
    }

    private final int decimals;

    private BigDecimalDecimalLimit(final int decimals) {
        this.decimals = decimals;
    }

    @Override
    public Optional<ValidationError> validate(final BigDecimal item) {
        if (item.scale() > decimals) {
            return Optional.of(ValidationError.of("Too many decimals " + item.toString() + " (only " + decimals + " allowed)"));
        }
        return Optional.empty();

    }

}
