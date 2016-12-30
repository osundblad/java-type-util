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
package se.eris.jtype.collection.math;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("CollectionAddedToSelf")
public class MathSetTest {

    private final SetCreator<String> creator = HashSetCreator.immutable();

    private Set<String> toSet(final String... items) {
        return creator.from(Arrays.asList(items));
    }

    /**
     * <pre>
     *     {1, 2}       \ {1, 2} = ∅
     *     {1, 2, 3, 4} \ {1, 3} = {2, 4}
     * </pre>
     */
    @Test
    public void complement_collection() {
        final MathSet<String> set12 = MathSet.of(creator, toSet("1", "2"));
        assertThat(set12.complement(set12), is(Collections.emptySet()));

        final MathSet<String> set1234 = MathSet.of(creator, toSet("1", "2", "3", "4"));
        assertThat(set1234.complement(toSet("1", "3")), is(toSet("2", "4")));
    }

    /**
     * <pre>
     *     {1, 2}       \ {1, 2} = ∅
     *     {1, 2, 3, 4} \ {1, 3} = {2, 4}
     * </pre>
     */
    @Test
    public void complement_varargs() {
        final MathSet<String> set12 = MathSet.of(creator, toSet("1", "2"));
        assertThat(set12.complement("1", "2"), is(Collections.emptySet()));

        final MathSet<String> set1234 = MathSet.of(creator, toSet("1", "2", "3", "4"));
        assertThat(set1234.complement("1", "3"), is(toSet("2", "4")));
    }

    /**
     * <pre>
     *     {1, 2} ∩ {1, 2} = {1, 2}
     *     {1, 2} ∩ {2, 3} = {2}
     * </pre>
     */
    @Test
    public void intersection() {
        final MathSet<String> set12 = MathSet.wrap(creator, toSet("1", "2"));
        assertThat(set12.intersection(set12), is(toSet("1", "2")));

        final MathSet<String> set23 = MathSet.wrap(creator, toSet("2", "3"));
        assertThat(set12.intersection(set23), is(toSet("2")));
    }

    /**
     * <pre>
     *     {1, 3, 5, 7} ∪ {1, 2, 4, 6} = {1, 2, 3, 4, 5, 6, 7}
     * </pre>
     */
    @Test
    public void union() {
        final MathSet<String> setOdd = MathSet.wrap(creator, toSet("1", "3", "5", "7"));
        final MathSet<String> setEven = MathSet.wrap(creator, toSet("2", "4", "6"));
        assertThat(setOdd.union(setEven), is(toSet("1", "2", "3", "4", "5", "6", "7")));
        assertThat(setEven.union(setOdd), is(toSet("1", "2", "3", "4", "5", "6", "7")));
    }

    /**
     * <pre>
     *     {1, 2, 3} ⊕ {3, 4} is {1, 2, 4}
     * </pre>
     */
    @Test
    public void disjunctiveUnion() {
        final MathSet<String> set123 = MathSet.wrap(creator, toSet("1", "2", "3"));
        final MathSet<String> set34 = MathSet.wrap(creator, toSet("3", "4"));
        assertThat(set123.disjunctiveUnion(set34), is(toSet("1", "2", "4")));
    }

    /**
     * <pre>
     *     {1, 2, 3} ⊕ {3, 4} is {1, 2, 4}
     * </pre>
     */
    @Test
    public void isSubset() {
        final MathSet<String> set12 = MathSet.wrap(creator, toSet("1", "2"));
        final MathSet<String> set123 = MathSet.wrap(creator, toSet("1", "2", "3"));
        final MathSet<String> set34 = MathSet.wrap(creator, toSet("3", "4"));

        final Set<String> set = Collections.singleton("e");
        set.containsAll(set);
        assertThat(set12.isSubset(set123), is(true));
        assertThat(set123.isSubset(set12), is(false));
        assertThat(set123.isSubset(set123), is(true));
        assertThat(set123.isSubset(set34), is(false));
    }

    /**
     * <pre>
     *     {1, 2, 3} ⊕ {3, 4} is {1, 2, 4}
     * </pre>
     */
    @Test
    public void isSuperset() {
        final MathSet<String> set12 = MathSet.wrap(creator, toSet("1", "2"));
        final MathSet<String> set123 = MathSet.wrap(creator, toSet("1", "2", "3"));
        final MathSet<String> set34 = MathSet.wrap(creator, toSet("3", "4"));

        assertThat(set12.isSuperset(set123), is(false));
        assertThat(set123.isSuperset(set12), is(true));
        assertThat(set123.isSuperset(set123), is(true));
        assertThat(set123.isSuperset(set34), is(false));
    }

}