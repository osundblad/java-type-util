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

/**
 * Two things that are meant to be used together.
 *
 * @param <T> the first type
 * @param <U> the second type
 *
 * @see PairWrapper
 */
public abstract class DyadWrapper<T, U> {

    @NotNull
    private final T first;
    @NotNull
    private final U second;

    protected DyadWrapper(@NotNull final T first, @NotNull final U second) {
        this.first = first;
        this.second = second;
    }

    @NotNull
    public T rawFirst() {
        return first;
    }

    @NotNull
    public U rawSecond() {
        return second;
    }

    @SuppressWarnings("ControlFlowStatementWithoutBraces")
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        final DyadWrapper<?,?> that = (DyadWrapper<?,?>) o;

        return first.equals(that.first) && second.equals(that.second);
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = (31 * result) + second.hashCode();
        return result;
    }

    @Override
    @NotNull
    public String toString() {
        return this.getClass().getSimpleName() + "{" + first + ", " + second + "}";
    }

}
