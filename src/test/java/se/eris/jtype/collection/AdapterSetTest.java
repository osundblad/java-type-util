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

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AdapterSetTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final Subject s1_1 = new Subject(1, "1");
    private static final Subject s1_2 = new Subject(1, "2");
    private static final Subject s2_2 = new Subject(2, "2");

    @Test
    public void from_nullItem() {
        AdapterSet.from(Collections.singleton(null), Subject.he);
    }

    @Test
    public void size() {
        final Set<Subject> hashSet = new HashSet<>(Arrays.asList(s1_1, s1_2, s2_2));
        assertThat(hashSet.size(), is(3));

        final AdapterSet<Subject> adapterSet = AdapterSet.from(hashSet, Subject.he);
        assertThat(adapterSet.size(), is(2));
    }

    @Test
    public void contains() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Collections.singletonList(s1_1), Subject.he);

        assertThat(adapterSet.contains(s1_1), is(true));
        assertThat(adapterSet.contains(s1_2), is(true));
        assertThat(adapterSet.contains(s2_2), is(false));
    }

    @Test
    public void contains_other() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Collections.singletonList(s1_1), Subject.he);

        assertThat(adapterSet.contains("a string"), is(false));

    }

    @Test
    public void iterator() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Arrays.asList(s1_1, s1_2, s2_2), Subject.he);

        final Iterator<Subject> iterator = adapterSet.iterator();
        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        assertThat(iterator.hasNext(), is(false));

        int c = 0;
        for (final Subject subject : adapterSet) {
            c++;
        }
        assertThat(c, is(2));
    }

    @Test
    public void add() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Collections.emptySet(), Subject.he);

        exception.expect(UnsupportedOperationException.class);
        adapterSet.add(s1_1);
    }

    @Test
    public void remove() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Collections.emptySet(), Subject.he);

        exception.expect(UnsupportedOperationException.class);
        adapterSet.remove(s1_1);
    }

    @Test
    public void addAll() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Collections.emptySet(), Subject.he);

        exception.expect(UnsupportedOperationException.class);
        adapterSet.addAll(Collections.emptyList());
    }

    @Test
    public void clear() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Collections.emptySet(), Subject.he);

        exception.expect(UnsupportedOperationException.class);
        adapterSet.clear();

    }

    @Test
    public void removeAll() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Collections.emptySet(), Subject.he);

        exception.expect(UnsupportedOperationException.class);
        adapterSet.removeAll(Collections.emptyList());
    }

    @Test
    public void retainAll() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Collections.emptySet(), Subject.he);

        exception.expect(UnsupportedOperationException.class);
        adapterSet.retainAll(Collections.emptyList());
    }

    @SuppressWarnings("CollectionAddedToSelf")
    @Test
    public void containsAll_empty() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Collections.emptySet(), Subject.he);

        assertThat(adapterSet.containsAll(adapterSet), is(true));
        assertThat(adapterSet.containsAll(Collections.emptySet()), is(true));
    }

    @Test
    public void containsAll_nonEmpty() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Collections.singleton(s1_1), Subject.he);

        assertThat(adapterSet.containsAll(Arrays.asList(s1_1, s1_2)), is(true));
        assertThat(adapterSet.containsAll(Arrays.asList(s1_1, s2_2)), is(false));
    }

    @Test
    public void toArray() {

    }

    @Test
    public void toArray1() {

    }

    @Test
    public void spliterator() {

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