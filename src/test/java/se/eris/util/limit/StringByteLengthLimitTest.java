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

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StringByteLengthLimitTest {

    @Test
    public void validate_inRange() {
        StringByteLengthLimit.of(2, 4).validate("12");
        StringByteLengthLimit.of(2, 4).validate("1234");
        StringByteLengthLimit.of(2, 2).validate("ö");
        StringByteLengthLimit.zeroTo(4).validate("öö");
    }

    @Test
    public void validate_toShort() {
        assertThat(StringByteLengthLimit.of(2, 4).validate("1").hasErrors(), is(true));
    }

    @Test
    public void validate_toLong() {
        assertThat(StringByteLengthLimit.zeroTo((1+2+2) - 1).validate("aåä").hasErrors(), is(true));
    }

    @Test
    public void validate_utf16_toLong() {
        assertThat(StringByteLengthLimit.of(0, (4+2+2) - 1, StandardCharsets.UTF_16).validate("abc").hasErrors(), is(true));
    }

}