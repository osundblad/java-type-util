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
package se.eris.jtype.type;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public abstract class OneOfBaseWrapper<T, U> implements Serializable {

    protected final SOptional<T> first;
    protected final SOptional<U> second;

    public OneOfBaseWrapper(@Nullable final U second, @Nullable final T first) {
        this.second = SOptional.ofNullable(second);
        this.first = SOptional.ofNullable(first);
    }

    @Nullable
    public T rawFirst() {
        return first.orNull();
    }

    public java.util.Optional<T> optionalFirst() {
        return first.asOptional();
    }

    @Nullable
    public U rawSecond() {
        return second.orNull();
    }

    public java.util.Optional<U> optionalSecond() {
        return second.asOptional();
    }

    @SuppressWarnings("ControlFlowStatementWithoutBraces")
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        final OneOfBaseWrapper<?, ?> that = (OneOfBaseWrapper<?, ?>) o;

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
        return this.getClass().getSimpleName() + "{" + first.orNull() + ", " + second.orNull() + "}";
    }

}
