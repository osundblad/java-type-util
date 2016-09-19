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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractLimited<T> {

    @NotNull
    private final ValidationBehavior validationBehavior;

    @NotNull
    private final List<Function<T, Optional<ValidationError>>> limits;

    protected AbstractLimited(@NotNull final List<Function<T, Optional<ValidationError>>> limits, @NotNull final ValidationBehavior validationBehavior) {
        this.limits = limits;
        this.validationBehavior = validationBehavior;
    }

    @NotNull
    public T of(@NotNull final T t) {
        final ValidationBehavior behavior = validationBehavior.instance();
        for (final Function<T, Optional<ValidationError>> limit : limits) {
            behavior.atValidation(limit.apply(t));
        }
        behavior.afterValidation();
        return t;
    }

    public abstract static class Builder<T> {

        @NotNull
        protected final List<Function<T, Optional<ValidationError>>> limits = new ArrayList<>();

        @NotNull
        protected final ValidationBehavior validationBehavior;

        public Builder(@NotNull final ValidationBehavior validationBehavior) {
            this.validationBehavior = validationBehavior;
        }

        public Builder() {
            this(new ValidationBehaviorThrowImmediately());
        }

        @NotNull
        public Builder<T> limit(@NotNull final Limit<T> limit) {
            limits.add(limit::validate);
            return this;
        }

    }

}
