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
import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
public class LimitedString extends AbstractLimited<String> {

    public static LimitedString.Builder init() {
        return new Builder();
    }

    public static Builder init(final ValidationBehavior validationBehavior) {
        return new Builder(validationBehavior);
    }

    @Override
    public String of(final String s) {
        return super.of(s);
    }

    private LimitedString(final List<Function<String, Optional<ValidationError>>> limits, final ValidationBehavior validationBehavior) {
        super(limits, validationBehavior);
    }

    @SuppressWarnings("WeakerAccess")
    public static class Builder extends AbstractLimited.Builder<String> {

        public Builder() {
            super();
        }

        public Builder(final ValidationBehavior validationBehavior) {
            super(validationBehavior);
        }

        public Builder limit(final Limit<String> limit) {
            return (Builder) super.limit(limit);
        }

        public Builder notEmpty() {
            limit(StringLengthLimit.notEmpty());
            return this;
        }

        public Builder length(final int min, final int max) {
            limit(StringLengthLimit.of(min, max));
            return this;
        }

        public Builder length(final int max) {
            limit(StringLengthLimit.zeroTo(max));
            return this;
        }

        public Builder matches(final Pattern pattern) {
            limit(StringRegexpLimit.of(pattern));
            return this;
        }

        public Builder matches(final String regexp) {
            limit(StringRegexpLimit.of(regexp));
            return this;
        }

        public LimitedString build() {
            return new LimitedString(limits, validationBehavior);
        }

    }

}
