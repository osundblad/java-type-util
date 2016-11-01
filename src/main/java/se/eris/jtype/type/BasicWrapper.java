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

public abstract class BasicWrapper<T> {

    @NotNull
    private final T item;

    @SuppressWarnings("WeakerAccess")
    protected BasicWrapper(@NotNull final T item) {
        this.item = item;
    }

    @SuppressWarnings("WeakerAccess")
    @NotNull
    public T raw() {
        return item;
    }

    @SuppressWarnings("ControlFlowStatementWithoutBraces")
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        final BasicWrapper that = (BasicWrapper) o;

        return item.equals(that.item);
    }

    @Override
    public int hashCode() {
        return item.hashCode();
    }

    @Override
    @NotNull
    public String toString() {
        return this.getClass().getSimpleName() + "{" + item + "}";
    }

}
