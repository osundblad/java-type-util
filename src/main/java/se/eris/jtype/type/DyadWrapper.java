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

import java.io.Serializable;

/**
 * Two things that are meant to be used together.
 *
 * @param <T> the first type
 * @param <U> the second type
 * @see PairWrapper
 */
public abstract class DyadWrapper<T, U> implements Serializable {

    private final T first;
    private final U second;

    protected DyadWrapper(final T first, final U second) {
        this.first = first;
        this.second = second;
    }

    public T rawFirst() {
        return first;
    }

    public U rawSecond() {
        return second;
    }

    @SuppressWarnings("ControlFlowStatementWithoutBraces")
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        final DyadWrapper<?, ?> that = (DyadWrapper<?, ?>) o;

        return first.equals(that.first) && second.equals(that.second);
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = (31 * result) + second.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + first + ", " + second + "}";
    }

}
