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

import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class HashcodeEqualsDecoratorTest {

    private static final Subject s1_1 = new Subject(1, "1");
    private static final Subject s1_2 = new Subject(1, "2");
    private static final Subject s2_2 = new Subject(2, "2");

    @Test
    public void equals() {
        assertThat(s1_1, not(s1_2));
        final HashcodeEqualsDecorator<Subject> of1_1 = HashcodeEqualsDecorator.of(s1_1, Subject.he);
        final HashcodeEqualsDecorator<Subject> of1_2 = HashcodeEqualsDecorator.of(s1_2, Subject.he);
        assertThat(of1_1, is(of1_2));
    }

    @Test
    public void hashcode_shouldFollowSetDefinition() {
        assertThat(HashcodeEqualsDecorator.of(s1_1, Subject.he).hashCode(), is(1));
        assertThat(HashcodeEqualsDecorator.of(s1_2, Subject.he).hashCode(), is(1));
        assertThat(HashcodeEqualsDecorator.of(s2_2, Subject.he).hashCode(), is(2));
    }

    private static class Subject {

        static final ToIntFunction<Subject> hashcodeFunction = o -> o.id;
        static final BiPredicate<Subject, Subject> equalsFunction = (o1, o2) -> o1.id == o2.id;
        static final HashcodeEquals<Subject> he = HashcodeEquals.of(hashcodeFunction, equalsFunction);

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
            return (name != null) ? name.equals(subject.name) : (subject.name == null);
        }

        @Override
        public int hashCode() {
            int result = id;
            result = (31 * result) + ((name != null) ? name.hashCode() : 0);
            return result;
        }
    }

}