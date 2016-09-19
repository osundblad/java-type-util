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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import se.eris.jtype.StringTestUtil;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static se.eris.jtype.limit.StringLengthLimit.LONGEST_STRING_TO_PRESENT;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class StringLengthLimitTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void of_minGreaterThanMax() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("greater than max");
        StringLengthLimit.of(2, 1);
    }

    @Test
    public void of_minNegative() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("less than zero");
        StringLengthLimit.of(-4, 1);
    }

    @Test
    public void zeroTo_allowsEmptyString() {
        StringLengthLimit.zeroTo(8).validate("");
    }

    @Test
    public void zeroTo_inRange() {
        StringLengthLimit.zeroTo(3).validate("123");
    }

    @Test
    public void validate_minEqualToMax() {
        assertThat(StringLengthLimit.of(2, 2).validate("ok").isPresent(), is(false));
    }

    @Test
    public void validate_inRange() {
        StringLengthLimit.of(2, 4).validate("12");
        StringLengthLimit.of(2, 4).validate("1234");
    }

    @Test
    public void validate_toShort() {
        assertThat(StringLengthLimit.of(2, 4).validate("1").isPresent(), is(true));
    }

    @Test
    public void validate_toLong() {
        final Optional<ValidationError> validationError = StringLengthLimit.of(2, 4).validate("12345");
        assertThat(validationError.isPresent(), is(true));
        assertThat(validationError.get().asString(), containsString("longer"));
    }

    @Test
    public void atLeast_ok() {
        final Optional<ValidationError> validationError = StringLengthLimit.atLeast(4).validate("12345");
        assertThat(validationError.isPresent(), is(false));
    }

    @Test
    public void atLeast_toShort() {
        final Optional<ValidationError> validationError = StringLengthLimit.atLeast(4).validate("123");
        assertThat(validationError.isPresent(), is(true));
    }

    @Test
    public void oneTo_ok() {
        final Optional<ValidationError> validationError = StringLengthLimit.oneTo(4).validate("1234");
        assertThat(validationError.isPresent(), is(false));
    }

    @Test
    public void oneTo_toLong() {
        final Optional<ValidationError> validationError = StringLengthLimit.oneTo(2).validate("123");
        assertThat(validationError.isPresent(), is(true));
        assertThat(validationError.get().asString(), containsString("'123'"));
    }

    @Test
    public void exactly_ok() {
        final Optional<ValidationError> validationError = StringLengthLimit.exactly(4).validate("1234");
        assertThat(validationError.isPresent(), is(false));
    }

    @Test
    public void exactly_toShort() {
        final Optional<ValidationError> validationError = StringLengthLimit.exactly(4).validate("123");
        assertThat(validationError.isPresent(), is(true));
        assertThat(validationError.get().asString(), containsString("'123'"));
    }

    @Test
    public void exactly_toLong() {
        final Optional<ValidationError> validationError = StringLengthLimit.exactly(4).validate("12345");
        assertThat(validationError.isPresent(), is(true));
        assertThat(validationError.get().asString(), containsString("'12345'"));
    }

    @Test
    public void errorFormat_truncateLongStrings() {
        final Optional<ValidationError> validationError = StringLengthLimit.oneTo(2).validate(StringTestUtil.createLongString(LONGEST_STRING_TO_PRESENT + 1));
        assertThat(validationError.isPresent(), is(true));
        assertThat(validationError.get().asString(), containsString("(truncated at "));
    }

    @Test
    public void errorFormat_doNotTruncateShortStrings() {
        final Optional<ValidationError> validationError = StringLengthLimit.oneTo(2).validate(StringTestUtil.createLongString(LONGEST_STRING_TO_PRESENT));
        assertThat(validationError.isPresent(), is(true));
        assertThat(validationError.get().asString(), not(containsString("(truncated at ")));
    }
}
