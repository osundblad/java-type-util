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

import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class MathSet<T> implements Set<T> {

    private final Set<T> set;
    private final SetCreator<T> creator;

    @SafeVarargs
    public static <E> MathSet<E> of(final SetCreator<E> creator, final E... items) {
        return of(creator, Arrays.asList(items));
    }

    public static <E> MathSet<E> of(final SetCreator<E> creator, final Collection<E> collection) {
        return wrap(creator, creator.from(collection));
    }

    public static <E> MathSet<E> wrap(final SetCreator<E> creator, final Set<E> set) {
        return new MathSet<>(creator, set);
    }

    private MathSet(final SetCreator<T> creator, final Set<T> set) {
        this.set = set;
        this.creator = creator;
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return set.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @NotNull
    @Override
    public <E> E[] toArray(final E[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean add(final T o) {
        return set.add(o);
    }

    @Override
    public boolean remove(final Object o) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(final Collection c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        return set.addAll(c);
    }

    @Override
    public boolean retainAll(final Collection c) {
        return set.retainAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(final Object o) {
        return set.equals(o);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @Override
    public Spliterator<T> spliterator() {
        return set.spliterator();
    }

    /**
     * <p>The relative complement.</p>
     *
     * <p>Examples:
     * <pre>
     *     {1, 2}       \ {1, 2} = ∅
     *     {1, 2, 3, 4} \ {1, 3} = {2, 4}
     * </pre>
     * </p>
     * @see #removeAll(Collection)
     */
    public MathSet<T> complement(final Collection<T> remove) {
        final Collection<T> newSet = new HashSet<>(this);
        newSet.removeAll(remove);
        return of(creator, newSet);
    }

    /**
     * @see #complement(Collection)
     */
    @SafeVarargs
    public final MathSet<T> complement(final T... remove) {
        return complement(Arrays.asList(remove));
    }

    /**
     * <p>Examples:
     * <pre>
     *     {1, 2} ∩ {1, 2} = {1, 2}
     *     {1, 2} ∩ {2, 3} = {2}
     * </pre>
     * </p>
     * @see #intersection(Object[])
     * @see #retainAll(Collection)
     */
    public MathSet<T> intersection(final Collection<T> keep) {
        final Collection<T> items = new HashSet<>(this);
        items.retainAll(keep);
        return of(creator, items);
    }

    /**
     * @see #intersection(Collection)
     * @see #retainAll(Collection)
     */
    @SafeVarargs
    public final MathSet<T> intersection(final T... keep) {
        return intersection(Arrays.asList(keep));
    }

    /**
     * <p>Examples:
     * <pre>
     *     {1, 3, 5, 7} ∪ {1, 2, 4, 6} = {1, 2, 3, 4, 5, 6, 7}
     *     {1, 2, 3} ∪ {1, 2, 3} = {1, 2, 3}
     * </pre>
     * @param add
     * @return
     */
    public MathSet<T> union(final Collection<T> add) {
        final Collection<T> newSet = new HashSet<>(2 * (this.size() + add.size()));
        newSet.addAll(this);
        newSet.addAll(add);
        return of(creator, newSet);
    }

    /**
     * <pre>
     *     {1, 2, 3} ⊕ {3, 4} is {1, 2, 4}
     * </pre>
     */
    public MathSet<T> disjunctiveUnion(final Collection<T> otherSet) {
        final MathSet<T> union = this.union(otherSet);
        final MathSet<T> intersection = this.intersection(otherSet);
        return union.complement(intersection);
    }

    @Override
    public String toString() {
        return set.toString();
    }

    /**
     * @param otherSet
     * @return true if thia is a subset of the supplied set, false otherwise.
     */
    public boolean isSubset(final Collection<T> otherSet) {
        return otherSet.containsAll(this.set);
    }

    /**
     * @param otherSet
     * @return true if thia is a superset of the supplied set, false otherwise.
     */
    public boolean isSuperset(final Collection<T> otherSet) {
        return this.set.containsAll(otherSet);
    }
}
