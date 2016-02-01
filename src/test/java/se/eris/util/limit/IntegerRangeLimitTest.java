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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntegerRangeLimitTest {

    @Test
    public void validate_inRange() {
        IntegerRangeLimit.of(2, 7).validate(2);
        IntegerRangeLimit.of(2, 7).validate(7);
    }

    @Test
    public void validate_toLow() {
        assertThat(IntegerRangeLimit.of(2, 7).validate(1).hasErrors(), is(true));
    }

    @Test
    public void validate_toHigh() {
        assertThat(IntegerRangeLimit.zeroTo(7).validate(8).hasErrors(), is(true));
    }

}