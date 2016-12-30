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
import java.util.Collections;
import java.util.Set;

public class ImmutableSetCreatorWrapper<T> implements SetCreator<T> {

    public static <T> SetCreator<T> of(final SetCreator<T> creator) {
        return new ImmutableSetCreatorWrapper<>(creator);
    }

    private final SetCreator<T> creator;

    private ImmutableSetCreatorWrapper(final SetCreator<T> creator) {
        this.creator = creator;
    }

    private Set<T> immutable(final Collection<T> collection) {
        return Collections.unmodifiableSet(creator.from(collection));
    }

    @Override
    public Set<T> from(final Collection<T> collection) {
        return immutable(collection);
    }

}
