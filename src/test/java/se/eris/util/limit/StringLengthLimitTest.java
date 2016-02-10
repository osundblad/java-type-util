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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StringLengthLimitTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void of_minHigherThanMax() {
        exception.expect(IllegalArgumentException.class);
        StringLengthLimit.of(4, 1);
    }

    @Test
    public void of_minNegative() {
        exception.expect(IllegalArgumentException.class);
        StringLengthLimit.of(-4, 1);
    }

    @Test
    public void max_allowsEmptyString() {
        StringLengthLimit.zeroTo(8).validate("");
    }

    @Test
    public void max_inRange() {
        StringLengthLimit.zeroTo(3).validate("123");
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
        assertThat(StringLengthLimit.of(2, 4).validate("12345").isPresent(), is(true));
    }

}