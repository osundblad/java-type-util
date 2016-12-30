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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Set;

public class ImmutableSetCreatorTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void of_varargs() {
        final Set<String> set = ImmutableSetCreator.of("A", "B");

        exception.expect(UnsupportedOperationException.class);
        set.remove("A");
    }

    @Test
    public void of_collection() {
        final Set<String> set = ImmutableSetCreator.of(Arrays.asList("A", "B"));

        exception.expect(UnsupportedOperationException.class);
        set.add("A");
    }

    @Test
    public void from() {
        final SetCreator<String> creator = new ImmutableSetCreator<>();
        final Set<String> set = creator.from(Arrays.asList("A", "B"));

        exception.expect(UnsupportedOperationException.class);
        set.clear();
    }

}