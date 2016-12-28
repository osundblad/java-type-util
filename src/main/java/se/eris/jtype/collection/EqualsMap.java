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

import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.eris.jtype.Experimental;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * An immutable map using the supplied hashcode and equals functions. Note that null keys are
 * not allowed in this map.
 *
 * @param <K>
 */
@Experimental
public class EqualsMap<K, V> implements Map<K, V>, Serializable {

    public static <K, V> EqualsMap<K, V> from(final Map<K, V> map, final HashcodeEquals<K> he) {
        return new EqualsMap<K, V>(map, he);
    }

    private final Map<HashcodeEqualsDecorator<K>, V> map;
    private final HashcodeEquals<K> he;

    private EqualsMap(final Map<K, V> map, final HashcodeEquals<K> he) {
        this.map = Collections.unmodifiableMap(removeDuplicateAndNullKeys(map, he));
        this.he = he;
    }

    @NotNull
    private Map<HashcodeEqualsDecorator<K>, V> removeDuplicateAndNullKeys(final Map<K, V> map, final HashcodeEquals<K> he) {
        final Map<HashcodeEqualsDecorator<K>, V> withourDuplicateOrNullKeys = new HashMap<>();
        for (final Entry<K, V> e : map.entrySet()) {
            if (e.getKey() != null) {
                withourDuplicateOrNullKeys.put(he.decorate(e.getKey()), e.getValue());
            }
        }
        return withourDuplicateOrNullKeys;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(@Nullable final Object key) {
        if (key == null) {
            return false;
        }
        try {
            return map.containsKey(he.decorate((K) key));
        } catch (final ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean containsValue(@Nullable final Object value) {
        return map.containsValue(value);
    }

    @Nullable
    @Override
    public V get(final Object key) {
        try {
            return map.get(he.decorate((K) key));
        } catch (final ClassCastException e) {
            return null;
        }
    }

    @Override
    public V put(@Flow(target = "this.keys", targetIsContainer = true) final K key, @Flow(target = "this.values", targetIsContainer = true) final V value) {
        unsupportedOperationImmutable();
        //noinspection ReturnOfNull
        return null;
    }

    @Override
    public V remove(final Object o) {
        unsupportedOperationImmutable();
        //noinspection ReturnOfNull
        return null;
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        unsupportedOperationImmutable();
    }

    @Override
    public void clear() {
        unsupportedOperationImmutable();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet().stream().map(HashcodeEqualsDecorator::getSubject).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet().stream()
                .map(e -> new AbstractMap.SimpleImmutableEntry<>(e.getKey().getSubject(), e.getValue()))
                .collect(Collectors.toSet());
    }

    private void unsupportedOperationImmutable() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is immutable");
    }

    @SuppressWarnings("ControlFlowStatementWithoutBraces")
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if ((o == null) || (!(o instanceof Map))) return false;

        final Map<?, ?> that = (Map<?, ?>) o;
        if (that.size() != this.size())
            return false;

        for (final Entry<?, ?> entry : that.entrySet()) {
            final Object entryKey = entry.getKey();
            if (!containsKey(entryKey)) {
                return false;
            }
            if (!Objects.equals(get(entryKey), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = map.hashCode();
        result = (31 * result) + he.hashCode();
        return result;
    }

    public Map<K, V> asMap() {
        return map.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().getSubject(), Map.Entry::getValue));
    }

    @Override
    public String toString() {
        return asMap().toString();
    }

}
