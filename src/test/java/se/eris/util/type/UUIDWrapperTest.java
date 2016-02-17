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
package se.eris.util.type;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UUIDWrapperTest {

    @Test
    public void asUUID() {
        final UUID uuid = UUID.randomUUID();
        final UUIDWrapperA wrapper = new UUIDWrapperA(uuid);

        assertThat(wrapper.asUUID(), is(uuid));
    }

    @Test
    public void asString() {
        final UUID uuid = UUID.randomUUID();
        final UUIDWrapperA wrapper = new UUIDWrapperA(uuid);

        assertThat(wrapper.asString(), is(uuid.toString()));
    }

    private static class UUIDWrapperA extends UUIDWrapper {

        private UUIDWrapperA(@NotNull final UUID uuid) {
            super(uuid);
        }

    }
}