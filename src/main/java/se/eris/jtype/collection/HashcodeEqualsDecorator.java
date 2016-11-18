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
package se.eris.jtype.collection;

import se.eris.jtype.Experimental;

import java.io.Serializable;

@Experimental
class HashcodeEqualsDecorator<T> implements Serializable {

    public static <T> HashcodeEqualsDecorator<T> of(final T subject, final HashcodeEquals<T> functions) {
        return new HashcodeEqualsDecorator<>(subject, functions);
    }

    private final T subject;
    private final HashcodeEquals<T> functions;

    private HashcodeEqualsDecorator(final T subject, final HashcodeEquals<T> functions) {
        this.subject = subject;
        this.functions = functions;
    }

    @SuppressWarnings({"ControlFlowStatementWithoutBraces", "unchecked"})
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if ((o == null) || (this.getClass() != o.getClass())) return false;

        final HashcodeEqualsDecorator<T> that = (HashcodeEqualsDecorator<T>) o;
        return functions.equalsFunction().test(this.subject, that.subject);
    }

    @Override
    public int hashCode() {
        return functions.hashcodeFunction().applyAsInt(subject);
    }

    T getSubject() {
        return subject;
    }

}
