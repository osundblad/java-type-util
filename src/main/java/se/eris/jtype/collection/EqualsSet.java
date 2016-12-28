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

import org.jetbrains.annotations.Nullable;
import se.eris.jtype.Experimental;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An immutable set using the supplied hashcode and equals functions. Note that null values are
 * not allowed in this set.
 *
 * @param <T> the type of the Set.
 *
 * idea(s):
 *  - since equals/hashcode are overridden a get method seems useful
 */
@Experimental
public class EqualsSet<T> implements Set<T>, Serializable {

    public static <T> EqualsSet<T> from(final HashcodeEquals<T> he, final Collection<T> collection) {
        return new EqualsSet<>(he, collection);
    }

    @SafeVarargs
    public static <T> EqualsSet<T> from(final HashcodeEquals<T> he, final T... items) {
        return new EqualsSet<>(he, Arrays.asList(items));
    }
    private final EqualsMap<T, T> data;

    private final HashcodeEquals<T> he;

    private EqualsSet(final HashcodeEquals<T> he, final Collection<T> collection) {
        this.he = he;
        final Map<T, T> map = collection.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
        this.data = EqualsMap.from(map, he);
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(@Nullable final Object o) {
        //noinspection SuspiciousMethodCalls
        return data.containsKey(o);
    }

    @Override
    public boolean containsAll(final Collection c) {
        //noinspection ObjectEquality
        if (this == c) {
            return true;
        }
        for (final Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return data.values().iterator();
    }

    @Override
    public boolean add(final Object o) {
        unsupportedOperationImmutable();
        return false;
    }

    @Override
    public boolean remove(final Object o) {
        unsupportedOperationImmutable();
        return false;
    }

    @Override
    public boolean addAll(final Collection c) {
        unsupportedOperationImmutable();
        return false;
    }

    @Override
    public void clear() {
        unsupportedOperationImmutable();
    }

    @Override
    public boolean removeAll(final Collection c) {
        unsupportedOperationImmutable();
        return false;
    }

    @Override
    public boolean retainAll(final Collection c) {
        unsupportedOperationImmutable();
        return false;
    }

    /**
     * <p>The relative complement.</p>
     *
     * <p>Examples:
     * <pre>
     *     {1, 2} \ {1, 2} = ∅
     *     {1, 2, 3, 4} \ {1, 3} = {2, 4}</pre>
     * </p>
     * @see #removeAll(Collection)
      */
    public EqualsSet<T> complement(final Collection<T> remove) {
        final Collection<T> newSet = new HashSet<>(this);
        newSet.removeAll(remove);
        return EqualsSet.from(he, newSet);
    }

    /**
     * @see #complement(Collection)
      */
    @SafeVarargs
    public final EqualsSet<T> complement(final T... remove) {
        return complement(Arrays.asList(remove));
    }

    /**
     * <p>Examples:
     * <pre>
     *     {1, 2} ∩ {1, 2} = {1, 2}
     *     {1, 2} ∩ {2, 3} = {2}
     * </pre>
     * </p>
     * @see #retainAll(Collection)
     */
    public EqualsSet<T> intersection(final Collection<T> keep) {
        final Collection<T> newSet = new HashSet<>(this);
        newSet.retainAll(keep);
        return EqualsSet.from(he, newSet);
    }

    /**
     * @see #intersection(Collection)
     */
    @SafeVarargs
    public final EqualsSet<T> intersection(final T... keep) {
        return intersection(Arrays.asList(keep));
    }

    public EqualsSet<T> union(final Collection<T> add) {
        final Collection<T> newSet = new HashSet<>(2 * (this.size() + add.size()));
        newSet.addAll(this);
        newSet.addAll(add);
        return EqualsSet.from(he, newSet);
    }


    @Override
    public Object[] toArray() {
        return data.values().toArray();
    }

    @Override
    public <A> A[] toArray(final A[] a) {
        return data.values().toArray(a);
    }

    @Override
    public Spliterator<T> spliterator() {
        return data.values().spliterator();
    }

    private void unsupportedOperationImmutable() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is immutable");
    }

    /**
     * @return returns the Set as a Set without the overridden equals and hashcode.
     */
    public Set<T> asSet() {
        return new HashSet<>(data.values());
    }

    @SuppressWarnings("ControlFlowStatementWithoutBraces")
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if ((o == null) || (!(o instanceof Set))) return false;

        final Collection<?> that = (Collection<?>) o;

        return (data.size() == that.size()) && containsAll(that);
    }

    @Override
    public int hashCode() {
        return data.values().hashCode();
    }

    @Override
    public String toString() {
        return "EqualsSet{" +
                "set=" + data.values() +
                ", he=" + he +
                '}';
    }

}
