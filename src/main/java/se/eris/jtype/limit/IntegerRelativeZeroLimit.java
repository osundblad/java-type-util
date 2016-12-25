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

import java.util.Optional;

@SuppressWarnings({"TypeMayBeWeakened", "WeakerAccess"})
public class IntegerRelativeZeroLimit implements Limit<Integer> {

    private enum ZeroRelation {
        /** For complete coverage. Kind of useless */
        ZERO,
        NON_ZERO,
        POSITIVE,
        NEGATIVE,
        NON_POSITIVE,
        NON_NEGATIVE,
    }

    private static final IntegerRelativeZeroLimit POSITIVE = new IntegerRelativeZeroLimit(ZeroRelation.POSITIVE);
    private static final IntegerRelativeZeroLimit NEGATIVE = new IntegerRelativeZeroLimit(ZeroRelation.NEGATIVE);
    private static final IntegerRelativeZeroLimit NON_NEGATIVE = new IntegerRelativeZeroLimit(ZeroRelation.NON_NEGATIVE);
    private static final IntegerRelativeZeroLimit NON_POSITIVE = new IntegerRelativeZeroLimit(ZeroRelation.NON_POSITIVE);
    private static final IntegerRelativeZeroLimit ZERO = new IntegerRelativeZeroLimit(ZeroRelation.ZERO);
    private static final IntegerRelativeZeroLimit NON_ZERO = new IntegerRelativeZeroLimit(ZeroRelation.NON_ZERO);

    public static Limit<Integer> positive() {
        return POSITIVE;
    }

    public static Limit<Integer> nonPositive() {
        return NON_POSITIVE;
    }

    public static Limit<Integer> negative() {
        return NEGATIVE;
    }

    public static Limit<Integer> nonNegative() {
        return NON_NEGATIVE;
    }

    public static Limit<Integer> zero() {
        return ZERO;
    }

    public static Limit<Integer> nonZero() {
        return NON_ZERO;
    }

    private final ZeroRelation relation;

    private IntegerRelativeZeroLimit( final ZeroRelation relation) {
        this.relation = relation;
    }

    @SuppressWarnings("ControlFlowStatementWithoutBraces")
    @Override
    public Optional<ValidationError> validate( final Integer item) {
        switch (relation) {
            case POSITIVE: if (item <= 0) return Optional.of(ValidationError.of(item + " is not positive")); break;
            case NEGATIVE: if (item >= 0) return Optional.of(ValidationError.of(item + " is not negative")); break;
            case NON_POSITIVE: if (item > 0) return Optional.of(ValidationError.of(item + " is positive")); break;
            case NON_NEGATIVE: if (item < 0) return Optional.of(ValidationError.of(item + " is negative")); break;
            case ZERO: if (item != 0) return Optional.of(ValidationError.of(item + " is not zero")); break;
            case NON_ZERO: if (item == 0) return Optional.of(ValidationError.of(item + " is zero")); break;
            default:
                throw new IllegalStateException("Unknown zero relation '" + relation + "'");
        }
        return Optional.empty();
    }

}
