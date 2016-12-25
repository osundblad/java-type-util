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

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class LimitedInteger extends AbstractLimited<Integer> {

    public static LimitedInteger.Builder init() {
        return new Builder();
    }

    @Override
    public Integer of( final Integer i) {
        return super.of(i);
    }

    private LimitedInteger( final List<Function<Integer, Optional<ValidationError>>> limits,  final ValidationBehavior validationBehavior) {
        super(limits, validationBehavior);
    }

    public static class Builder extends AbstractLimited.Builder<Integer> {

        public Builder limit( final Limit<Integer> limit) {
            return (Builder) super.limit(limit);
        }

        public Builder zeroTo(final int max) {
            limit(IntegerRangeLimit.zeroTo(max));
            return this;
        }

        public Builder range(final int min, final int max) {
            limit(IntegerRangeLimit.of(min, max));
            return this;
        }

        /**
         * (0, MaxInt] or maybe more readable [1, MaxInt]
         * @return the Builder
         */
        public Builder positive() {
            limit(IntegerRelativeZeroLimit.positive());
            return this;
        }

        /**
         * [MinInt, 0]
         * @return the Builder
         */
        public Builder nonPositive() {
            limit(IntegerRelativeZeroLimit.nonPositive());
            return this;
        }

        /**
         * [MinInt, 0) or maybe more readable [MinInt, -1]
         * @return the Builder
         */
        public Builder negative() {
            limit(IntegerRelativeZeroLimit.negative());
            return this;
        }

        /**
         * [0, MaxInt]
         * @return the Builder
         */
        public Builder nonNegative() {
            limit(IntegerRelativeZeroLimit.nonNegative());
            return this;
        }

        public LimitedInteger build() {
            return new LimitedInteger(limits, validationBehavior);
        }
    }

}
