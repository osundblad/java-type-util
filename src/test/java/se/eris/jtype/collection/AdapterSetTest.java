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
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;

public class AdapterSetTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final Subject s1_1 = new Subject(1, "1");
    private static final Subject s1_2 = new Subject(1, "2");
    private static final Subject s2_2 = new Subject(2, "2");

    @Test
    public void size() {
        final Set<Subject> hashSet = new HashSet<>(Arrays.asList(s1_1, s1_2, s2_2));
        assertThat(hashSet.size(), is(3));

        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, hashSet);
        assertThat(adapterSet.size(), is(2));
    }

    @Test
    public void contains_() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, Collections.singletonList(s1_1));

        assertThat(adapterSet.contains(s1_1), is(true));
        assertThat(adapterSet.contains(s1_2), is(true));
        assertThat(adapterSet.contains(s2_2), is(false));
    }

    @Test
    public void contains_other() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, Collections.singletonList(s1_1));

        assertThat(adapterSet.contains("a string"), is(false));

    }

    @Test
    public void iterator() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, Arrays.asList(s1_1, s1_2, s2_2));

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
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, Collections.emptySet());

        exception.expect(UnsupportedOperationException.class);
        adapterSet.add(s1_1);
    }

    @Test
    public void remove() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, Collections.emptySet());

        exception.expect(UnsupportedOperationException.class);
        adapterSet.remove(s1_1);
    }

    @Test
    public void addAll() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, Collections.emptySet());

        exception.expect(UnsupportedOperationException.class);
        adapterSet.addAll(Collections.emptyList());
    }

    @Test
    public void clear() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, Collections.emptySet());

        exception.expect(UnsupportedOperationException.class);
        adapterSet.clear();

    }

    @Test
    public void removeAll() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, Collections.emptySet());

        exception.expect(UnsupportedOperationException.class);
        adapterSet.removeAll(Collections.emptyList());
    }

    @Test
    public void retainAll() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, Collections.emptySet());

        exception.expect(UnsupportedOperationException.class);
        adapterSet.retainAll(Collections.emptyList());
    }

    @SuppressWarnings("CollectionAddedToSelf")
    @Test
    public void containsAll_empty() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, Collections.emptySet());

        assertThat(adapterSet.containsAll(adapterSet), is(true));
        assertThat(adapterSet.containsAll(Collections.emptySet()), is(true));
    }

    @Test
    public void containsAll_nonEmpty() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, Collections.singleton(s1_1));

        assertThat(adapterSet.containsAll(Arrays.asList(s1_1, s1_2)), is(true));
        assertThat(adapterSet.containsAll(Arrays.asList(s1_1, s2_2)), is(false));
    }

    @Test
    public void toArray_withoutSuppliedArray() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, s1_1, s2_2);

        final Object[] array = adapterSet.toArray();
        assertThat(array, arrayContainingInAnyOrder(s1_1, s2_2));
        assertThat(array.length, is(2));
    }

    @Test
    public void toArray_suppliedArraySameLength() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, s1_1, s2_2);

        final Subject[] array2 = new Subject[2];
        final Object[] return2 = adapterSet.toArray(array2);
        assertThat(return2, arrayContainingInAnyOrder(s1_1, s2_2));
        assertThat(return2, sameInstance(array2));
    }

    @Test
    public void toArray_suppliedArrayLonger() {
        final AdapterSet<Subject> adapterSet = AdapterSet.from(Subject.he, s1_1, s2_2);

        final Subject[] array3 = new Subject[3];
        final Object[] return3 = adapterSet.toArray(array3);
        assertThat(return3, arrayContainingInAnyOrder(s1_1, s2_2, null));
        assertThat(return3, sameInstance(array3));
    }

    @Test
    public void spliterator() {
    }

    @Test
    public void setEquals() {
        final Set<Subject> adapterSet = AdapterSet.from(Subject.he, s1_1, s2_2);
        final Set<Subject> hashSet = new HashSet<>(Arrays.asList(s1_1, s2_2));

        assertThat(adapterSet, is(hashSet));
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