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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LimitedStringTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void notEmpty_validLength_returnsString() {
        assertThat(LimitedString.init().notEmpty().build().of("hi"), is("hi"));
    }

    @Test
    public void notEmpty_invalidLength_fails() {
        exception.expect(ValidationExceptionMatcher.of("shorter"));
        LimitedString.init().notEmpty().build().of("");
    }

    @Test
    public void of_validLength_returnsString() {
        assertThat(LimitedString.init().length(2, 5).build().of("hi"), is("hi"));
        assertThat(LimitedString.init().length(5).build().of("hello"), is("hello"));
    }

    @Test
    public void of_toShort_fails() {
        exception.expect(ValidationExceptionMatcher.of("shorter"));
        LimitedString.init().length(3, 5).build().of("hi");
    }

    @Test
    public void of_toLong_fails() {
        exception.expect(ValidationExceptionMatcher.of("longer"));
        LimitedString.init().length(4).build().of("hello");
    }

}