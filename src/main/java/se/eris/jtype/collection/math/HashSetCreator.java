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

import java.util.Collection;
import java.util.HashSet;

public class HashSetCreator<E> implements SetCreator<E> {

    private static final HashSetCreator<?> mutable = new HashSetCreator<>();
    private static final SetCreator<?> immutable = ImmutableSetCreatorWrapper.of(new HashSetCreator<>());

    public static <T> HashSet<T> newHashSet(final Collection<T> collection) {
        return new HashSet<>(collection);
    }

    public static <T> SetCreator<T> mutable() {
        return (SetCreator<T>) mutable;
    }

    public static <T> SetCreator<T> immutable() {
        return (SetCreator<T>) immutable;
    }

    private HashSetCreator() {
    }

    @Override
    public HashSet<E> from(final Collection<E> collection) {
        return newHashSet(collection);
    }

}
