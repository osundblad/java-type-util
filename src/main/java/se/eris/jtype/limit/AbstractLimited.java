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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractLimited<T> implements Serializable {

    private final ValidationBehavior validationBehavior;

    private final List<Function<T, Optional<ValidationError>>> limits;

    protected AbstractLimited(final List<Function<T, Optional<ValidationError>>> limits, final ValidationBehavior validationBehavior) {
        this.limits = limits;
        this.validationBehavior = validationBehavior;
    }

    public T of(final T t) {
        final ValidationBehavior behavior = validationBehavior.getInstance();
        for (final Function<T, Optional<ValidationError>> limit : limits) {
            behavior.atValidation(limit.apply(t));
        }
        behavior.afterValidation();
        return t;
    }

    public abstract static class Builder<T> {

        protected final List<Function<T, Optional<ValidationError>>> limits = new ArrayList<>();

        protected final ValidationBehavior validationBehavior;

        /**
         * Creates a Builder with the specified {@link ValidationBehavior}.
         *
         * @param validationBehavior the {@link ValidationBehavior} to use.
         */
        public Builder(final ValidationBehavior validationBehavior) {
            this.validationBehavior = validationBehavior;
        }

        /**
         * Creates a Builder with {@link ValidationBehaviorThrowImmediately} behavior.
         */
        public Builder() {
            this(new ValidationBehaviorThrowImmediately());
        }

        public Builder<T> limit(final Limit<T> limit) {
            limits.add(limit::validate);
            return this;
        }

    }

}
