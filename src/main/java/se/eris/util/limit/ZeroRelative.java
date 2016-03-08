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

import java.util.Optional;

public class ZeroRelative implements Limit<Integer> {

    private enum ZeroRelation {
        /** For complete coverage. Kind of useless */
        ZERO,
        POSITIVE,
        NEGATIVE,
        NON_POSITIVE,
        NON_NEGATIVE,
    }
    @NotNull
    public static final ZeroRelative POSITIVE = new ZeroRelative(ZeroRelation.POSITIVE);

    @NotNull
    public static final ZeroRelative NEGATIVE = new ZeroRelative(ZeroRelation.NEGATIVE);

    @NotNull
    public static final ZeroRelative NON_NEGATIVE = new ZeroRelative(ZeroRelation.NON_NEGATIVE);

    @NotNull
    public static final ZeroRelative NON_POSITIVE = new ZeroRelative(ZeroRelation.NON_POSITIVE);

    @NotNull
    public static final ZeroRelative ZERO = new ZeroRelative(ZeroRelation.ZERO);

    @NotNull
    public static Limit<Integer> positive() {
        return POSITIVE;
    }

    @NotNull
    public static Limit<Integer> nonPositive() {
        return NON_POSITIVE;
    }

    @NotNull
    public static Limit<Integer> negative() {
        return NEGATIVE;
    }

    @NotNull
    public static Limit<Integer> nonNegative() {
        return NON_NEGATIVE;
    }

    @NotNull
    public static Limit<Integer> zero() {
        return ZERO;
    }

    @NotNull
    private final ZeroRelation relation;

    private ZeroRelative(@NotNull final ZeroRelation relation) {
        this.relation = relation;
    }

    @Override
    public @NotNull Optional<ValidationError> validate(@NotNull final Integer item) {
        switch (relation) {
            case POSITIVE: if (item <= 0) return Optional.of(ValidationError.of(item + " is not positive")); break;
            case NEGATIVE: if (item >= 0) return Optional.of(ValidationError.of(item + " is not negative")); break;
            case NON_POSITIVE: if (item > 0) return Optional.of(ValidationError.of(item + " is positive")); break;
            case NON_NEGATIVE: if (item < 0) return Optional.of(ValidationError.of(item + " is negative")); break;
            case ZERO: if (item != 0) return Optional.of(ValidationError.of(item + " is not zero")); break;
            default:
                throw new IllegalStateException("Unknown relation '" + relation + "'");
        }
        return Optional.empty();
    }

}
