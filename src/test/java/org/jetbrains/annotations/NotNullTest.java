/*
 *    Copyright 2016 Olle Sundblad
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
package org.jetbrains.annotations;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SuppressWarnings("ConstantConditions")
public class NotNullTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void notNullArgument_shouldInsertNullCheck() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("@NotNull");
        notNullArgument(null);
    }

    @SuppressWarnings("EmptyMethod")
    private void notNullArgument(@NotNull final String s) {
    }

    @Test
    public void notNullReturnValue_shouldInsertNullCheck() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("@NotNull");
        notNullReturnValue();
    }

    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    @NotNull
    private String notNullReturnValue() {
        return null;
    }

}
