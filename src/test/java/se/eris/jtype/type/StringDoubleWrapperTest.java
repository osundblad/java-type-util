/*
 *    Copyright 2016 Olle Sundblad - olle@eris.se
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
package se.eris.jtype.type;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StringDoubleWrapperTest {

    @Test
    public void asString() {
        assertThat(WrapperImpl.of("3.333333333333333333").asString(), is("3.333333333333333333"));
    }

    private static class WrapperImpl extends StringDoubleWrapper {

        public static StringDoubleWrapperTest.WrapperImpl of(final String s) {
            return new WrapperImpl(s);
        }

        private WrapperImpl(final String s) {
            super(s);
        }
    }
}