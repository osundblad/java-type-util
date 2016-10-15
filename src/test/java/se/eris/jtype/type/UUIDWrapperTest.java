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
package se.eris.jtype.type;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UUIDWrapperTest {

    @Test
    public void asUUID() {
        final UUID uuid = UUID.randomUUID();
        final WrapperImpl wrapper = new WrapperImpl(uuid.toString());

        assertThat(wrapper.asUUID(), is(uuid));
    }

    @Test
    public void asString() {
        final UUID uuid = UUID.randomUUID();
        final WrapperImpl wrapper = new WrapperImpl(uuid);

        assertThat(wrapper.asString(), is(uuid.toString()));
    }

    private static final class WrapperImpl extends UUIDWrapper {

        private WrapperImpl(@NotNull final String uuid) {
            super(uuid);
        }

        private WrapperImpl(@NotNull final UUID uuid) {
            super(uuid);
        }

    }
}