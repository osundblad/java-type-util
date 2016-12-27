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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class DyadWrapperTest {

    @Test
    public void equals() {
        final Subject testSeven = new Subject("test", 7);
        final Subject testSevenNew = new Subject("test", 7);
        final Subject otherSeven = new Subject("other", 7);
        final Subject testEight = new Subject("test", 8);
        assertThat(testSeven, is(testSeven));
        assertThat(testSeven, is(testSevenNew));
        assertThat(testSeven, not(testEight));
        assertThat(testSeven, not(otherSeven));
    }

    private static class Subject extends DyadWrapper<String, Integer> {
        Subject(final String first, final Integer second) {
            super(first, second);
        }
    }

}