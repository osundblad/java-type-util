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

import java.util.UUID;

public abstract class UUIDWrapper extends BasicWrapper<UUID> {

    protected UUIDWrapper() {
        super(UUID.randomUUID());
    }

    protected UUIDWrapper(@NotNull final UUID uuid) {
        super(uuid);
    }

    @NotNull
    public UUID asUUID() {
        return super.raw();
    }

    @NotNull
    public String asString() { return this.raw().toString(); }

}
