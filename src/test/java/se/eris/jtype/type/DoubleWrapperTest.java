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

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DoubleWrapperTest {

    @Test
    public void asDouble() {
        final Double value = 1.2;
        final Subject subject = new Subject(value);

        assertThat(subject.asDouble(), is(value));
    }

    @Test
    public void asPrimitive() {
        final double value = 1.2;
        final Subject subject = new Subject(value);

        assertThat(subject.asPrimitive(), is(value));
    }

    @Test
    public void asString() {
        final String stringDouble = "1.2";
        final Double value = Double.valueOf(stringDouble);
        final Subject subject = new Subject(value);

        assertThat(subject.asString(), is(stringDouble));
    }

    private static class Subject extends DoubleWrapper {

        Subject(@NotNull final Double i) {
            super(i);
        }

    }


}