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

import java.util.List;
import java.util.function.Function;

public class LimitedInteger extends AbstractLimited<Integer> {

    @NotNull
    public static LimitedInteger.Builder init() {
        return new Builder();
    }

    @NotNull
    @Override
    public Integer of(@NotNull final Integer i) {
        return super.of(i);
    }

    private LimitedInteger(@NotNull final List<Function<Integer, ValidationMessages>> limits, @NotNull final ValidationBehavior validationBehavior) {
        super(limits, validationBehavior);
    }

    public static class Builder extends AbstractLimited.Builder<Integer> {

        @NotNull
        public Builder range(final int max) {
            limit(IntegerRangeLimit.zeroTo(max));
            return this;
        }

        @NotNull
        public Builder range(final int min, final int max) {
            limit(IntegerRangeLimit.of(min, max));
            return this;
        }

        @NotNull
        public LimitedInteger build() {
            return new LimitedInteger(limits, validationBehavior);
        }
    }

}
