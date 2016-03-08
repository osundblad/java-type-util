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
import static org.junit.Assert.assertThat;

public class ZeroRelativeTest {

    @Test
    public void positive_ok() {
        assertThat(ZeroRelative.positive().validate(1).isPresent(), is(false));
    }

    @Test
    public void positive_fail() {
        assertThat(ZeroRelative.positive().validate(0).isPresent(), is(true));
    }

    @Test
    public void nonPositive_ok() {
        assertThat(ZeroRelative.nonPositive().validate(0).isPresent(), is(false));
    }

    @Test
    public void nonPositive_fail() {
        assertThat(ZeroRelative.nonPositive().validate(1).isPresent(), is(true));
    }

    @Test
    public void negative_ok() {
        assertThat(ZeroRelative.negative().validate(-1).isPresent(), is(false));
    }

    @Test
    public void negative_fail() {
        assertThat(ZeroRelative.negative().validate(0).isPresent(), is(true));
    }

    @Test
    public void nonNegative_ok() {
        assertThat(ZeroRelative.nonNegative().validate(0).isPresent(), is(false));
    }

    @Test
    public void nonNegative_fail() {
        assertThat(ZeroRelative.nonNegative().validate(-1).isPresent(), is(true));
    }

}