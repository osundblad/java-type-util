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
package se.eris.jtype.type;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PairWrapperTest {

    @Test
    public void rawFirst() {
        assertThat(PairWrapperA.of("a", "b").rawFirst(), is("a"));
        assertThat(PairWrapperA.of("a", "b").rawSecond(), is("b"));
    }

    @Test
    public void hashCode_shouldWork() {
        assertThat(PairWrapperA.of("a", "b").hashCode(), is(PairWrapperA.of("a", "b").hashCode()));
        assertThat(PairWrapperA.of("a", "b").hashCode(), not(PairWrapperA.of("b", "a").hashCode()));
    }

    @Test
    public void equals_shouldWork() {
        final PairWrapperA a = PairWrapperA.of("a", "b");
        assertThat(a, is(a));
        assertThat(a, is(PairWrapperA.of("a", "b")));

        assertThat(a, not(PairWrapperA.of("b", "a")));
    }

    @Test
    public void equals_differentStringWrapperClasses_shouldDiffer() {
        final PairWrapperA a = PairWrapperA.of("a", "b");
        final PairWrapperB b = PairWrapperB.of("a", "b");

        assertThat(a, not(b));
    }

    @Test
    public void toString_showsClassName() {
        assertThat(PairWrapperA.of("a", "b").toString(), is("PairWrapperA{a, b}"));
    }

    private static class PairWrapperA extends PairWrapper<String> {

        private static PairWrapperA of(final String s1, final String s2) {
            return new PairWrapperA(s1, s2);
        }

        private PairWrapperA(final String s1, final String s2) {
            super(s1, s2);
        }
    }

    private static class PairWrapperB extends PairWrapper<String> {

        private static PairWrapperB of(final String s1, final String s2) {
            return new PairWrapperB(s1, s2);
        }

        private PairWrapperB(final String s1, final String s2) {
            super(s1, s2);
        }
    }

}