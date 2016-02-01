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
package se.eris.util.type;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StringWrapperTest {

    @Test
    public void asString() {
        assertThat(StringWrapperA.of("a").asString(), is("a"));
        assertThat(StringWrapperA.of("123").asString(), is("123"));
    }

    private static class StringWrapperA extends StringWrapper {

        private static StringWrapperA of(@NotNull final String s) {
            return new StringWrapperA(s);
        }

        private StringWrapperA(@NotNull final String s) {
            super(s);
        }
    }

}