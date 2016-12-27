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
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public class LimitedBigDecimal extends AbstractLimited<BigDecimal> {

    public static LimitedBigDecimal.Builder init() {
        return new Builder();
    }

    @Override
    public BigDecimal of( final BigDecimal bigDecimal) {
        return super.of(bigDecimal);
    }

    private LimitedBigDecimal( final Builder builder) {
        this(builder.limits, builder.validationBehavior);
    }

    private LimitedBigDecimal( final List<Function<BigDecimal, Optional<ValidationError>>> limits,  final ValidationBehavior validationBehavior) {
        super(limits, validationBehavior);
    }

    public static class Builder extends AbstractLimited.Builder<BigDecimal> {

        public Builder limit( final Limit<BigDecimal> limit) {
            return (Builder) super.limit(limit);
        }

        public Builder decimals(final int decimals) {
            limit(BigDecimalDecimalLimit.of(decimals));
            return this;
        }

        public Builder range( final String min,  final String max) {
            limit(BigDecimalRangeLimit.of(new BigDecimal(min), new BigDecimal(max)));
            return this;
        }

        public LimitedBigDecimal build() {
            return new LimitedBigDecimal(this);
        }
    }

}
