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
import java.util.regex.Pattern;

public class LimitedString extends AbstractLimited<String> {

    @NotNull
    public static LimitedString.Builder init() {
        return new Builder();
    }

    @NotNull
    @Override
    public String of(@NotNull final String s) {
        return super.of(s);
    }

    private LimitedString(@NotNull final List<Function<String, ValidationErrors>> limits, @NotNull final ValidationBehavior validationBehavior) {
        super(limits, validationBehavior);
    }

    public static class Builder extends AbstractLimited.Builder<String> {

        @NotNull
        public Builder length(final int min, final int max) {
            limit(StringLengthLimit.of(min, max));
            return this;
        }

        @NotNull
        public Builder length(final int max) {
            limit(StringLengthLimit.zeroTo(max));
            return this;
        }

        @NotNull
        public Builder matches(@NotNull final Pattern pattern) {
            limit(StringRegexpLimit.of(pattern));
            return this;
        }

        @NotNull
        public Builder matches(@NotNull final String regexp) {
            limit(StringRegexpLimit.of(regexp));
            return this;
        }

        @NotNull
        public LimitedString build() {
            return new LimitedString(limits, validationBehavior);
        }

    }

}
