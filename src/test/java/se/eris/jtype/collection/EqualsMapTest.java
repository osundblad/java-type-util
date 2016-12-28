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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class EqualsMapTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final Subject s1_1 = new Subject(1, "1");
    private static final Subject s1_2 = new Subject(1, "2");
    private static final Subject s2_2 = new Subject(2, "2");


    @Test
    public void equals_otherMapType() {
        final Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("AA", 2);
        final EqualsMap<String, Integer> equalsMap = EqualsMap.from(map, HashcodeEquals.of(String::length));

        assertThat(equalsMap, is(map));
    }

    @Test
    public void equals_otherWithNull_notEqual() {
        final Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("AA", 2);
        final EqualsMap<String, Integer> equalsMap = EqualsMap.from(map, HashcodeEquals.of(String::length));
        map.put("null", null);

        assertThat(equalsMap, not(map));
    }

    @Test
    public void containsKey_() {
        final EqualsMap<Subject, Integer> equalsMap = EqualsMap.from(Collections.singletonMap(s1_1, 1), Subject.he);

        assertThat(equalsMap.containsKey(s1_1), is(true));
        assertThat(equalsMap.containsKey(s1_2), is(true));
        assertThat(equalsMap.containsKey(s2_2), is(false));
        assertThat(equalsMap.containsKey("a"), is(false));
        assertThat(equalsMap.containsKey(null), is(false));
    }

    @Test
    public void contains_other() {
        final EqualsSet<Subject> equalsSet = EqualsSet.from(Subject.he, Collections.singletonList(s1_1));

        //noinspection SuspiciousMethodCalls
        assertThat(equalsSet.contains("a string"), is(false));
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
            return name.equals(subject.name);
        }

        @Override
        public int hashCode() {
            int result = id;
            result = (31 * result) + name.hashCode();
            return result;
        }
    }

}