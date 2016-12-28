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
package se.eris.jtype.collection;

import org.junit.Test;

import java.util.function.ToIntFunction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class HashcodeEqualsTest {

    @Test
    public void ofFunction() {
        final HashcodeEquals<Subject> hashcodeEquals = HashcodeEquals.ofFunction(s -> Integer.valueOf(s.name) % 2);

        final Subject s12 = new Subject(1, "2");
        final Subject s24 = new Subject(2, "4");
        assertThat(s12, not(s24));
        final HashcodeEqualsDecorator<Subject> of12 = HashcodeEqualsDecorator.of(s12, hashcodeEquals);
        final HashcodeEqualsDecorator<Subject> of13 = HashcodeEqualsDecorator.of(s24, hashcodeEquals);
        assertThat(of12, is(of13));
    }

    @Test
    public void ofHashcode() {
        final HashcodeEquals<Subject> hashcodeEquals = HashcodeEquals.ofHashcode();

        final Subject s12 = new Subject(1, "2");
        final Subject s13 = new Subject(1, "3");
        assertThat(s12, not(s13));
        final HashcodeEqualsDecorator<Subject> of12 = HashcodeEqualsDecorator.of(s12, hashcodeEquals);
        final HashcodeEqualsDecorator<Subject> of13 = HashcodeEqualsDecorator.of(s13, hashcodeEquals);
        assertThat(of12, is(of13));
    }

    @Test
    public void ofObject() {
        final HashcodeEquals<Subject> ofObject = HashcodeEquals.ofObject();

        final Subject s12 = new Subject(1, "2");
        final Subject s12_new = new Subject(1, "2");
        final Subject s13 = new Subject(1, "3");
        assertThat(s12, not(s13));
        final HashcodeEqualsDecorator<Subject> of12 = HashcodeEqualsDecorator.of(s12, ofObject);
        final HashcodeEqualsDecorator<Subject> of12_new = HashcodeEqualsDecorator.of(s12, ofObject);
        final HashcodeEqualsDecorator<Subject> of13 = HashcodeEqualsDecorator.of(s13, ofObject);
        assertThat(of12, is(of12));
        assertThat(of12_new, is(of12));
        assertThat(of12, not(of13));
    }

    private static class Subject {

        static final ToIntFunction<Subject> hashcodeFunction = o -> o.id;

        private final int id;
        private final String name;

        private Subject(final int id, final String name) {
            this.id = id;
            this.name = name;
        }

        @SuppressWarnings("ControlFlowStatementWithoutBraces")
        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if ((o == null) || (getClass() != o.getClass())) return false;

            final Subject subject = (Subject) o;

            if (id != subject.id) return false;
            return name.equals(subject.name);
        }

        @Override
        public int hashCode() {
            int result = id;
            result = (31 * result) + name.length();
            return result;
        }
    }


}