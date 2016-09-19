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

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LimitedBigDecimalTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void of() {
        final BigDecimal arbitraryBigDecimal = new BigDecimal("17");
        final LimitedBigDecimal limitedBigDecimal = LimitedBigDecimal.init().build();
        assertThat(limitedBigDecimal.of(arbitraryBigDecimal), is(arbitraryBigDecimal));
    }

    @Test
    public void range() {
        final BigDecimal arbitraryBigDecimal = new BigDecimal("17");
        final BigDecimal arbitraryBigDecimalPlusOne = new BigDecimal("18");
        final BigDecimal other = new BigDecimal("17.9");
        final LimitedBigDecimal limitedBigDecimal = LimitedBigDecimal.init().range("17", "18").build();

        assertThat(limitedBigDecimal.of(arbitraryBigDecimal), is(arbitraryBigDecimal));
        assertThat(limitedBigDecimal.of(arbitraryBigDecimalPlusOne), is(arbitraryBigDecimalPlusOne));
        assertThat(limitedBigDecimal.of(other), is(other));
    }

    @Test
    public void range_lessThanMin() {
        final BigDecimal outOfRange = new BigDecimal("16.9");
        final LimitedBigDecimal limitedBigDecimal = LimitedBigDecimal.init().range("17", "18").build();

        exception.expect(ValidationExceptionMatcher.of("is less than min"));
        limitedBigDecimal.of(outOfRange);
    }

    @Test
    public void range_greaterThanMax() {
        final BigDecimal outOfRange = new BigDecimal("18.00001");
        final LimitedBigDecimal limitedBigDecimal = LimitedBigDecimal.init().range("17", "18").build();

        exception.expect(ValidationExceptionMatcher.of("is greater than max"));
        limitedBigDecimal.of(outOfRange);
    }

}