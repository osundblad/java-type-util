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
 * BETA
 * An immutable map using the supplied hashcode and equals functions. Note that null keys are
 * not allowed in this map.
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
        this.map = Collections.unmodifiableMap(
                map.entrySet().stream().filter(e -> e.getKey() != null).collect(Collectors.toMap(
                        e -> HashcodeEqualsDecorator.of(e.getKey(), he),
                        Map.Entry::getValue)));
        this.he = he;
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
            throw new NullPointerException("null keys not allowed in EqualsMap");
        }
        return map.keySet().contains(HashcodeEqualsDecorator.of((K) key, he));
    }

    @Override
    public boolean containsValue(@Nullable final Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        return map.get(HashcodeEqualsDecorator.of((K) key, he));
    }

    @Override
    public V put(@Flow(target = "this.keys", targetIsContainer = true) final K key, @Flow(target = "this.values", targetIsContainer = true) final V value) {
        unsupportedOperationImmutable();
        return null;
    }

    @Override
    public V remove(final Object o) {
        unsupportedOperationImmutable();
        return null;
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        unsupportedOperationImmutable();
    }

    @Override
    public void clear() {
        unsupportedOperationImmutable();
        return;
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return map.keySet().stream().map(HashcodeEqualsDecorator::getSubject).collect(Collectors.toSet());
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return map.values();
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet().stream().map(e -> new AbstractMap.SimpleImmutableEntry<>(e.getKey().getSubject(), e.getValue())).collect(Collectors.toSet());
    }

    private void unsupportedOperationImmutable() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is immutable");
    }

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
        result = 31 * result + he.hashCode();
        return result;
    }
}
