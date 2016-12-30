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
package se.eris.jtype.collection.equals;

import se.eris.jtype.Experimental;
import se.eris.jtype.type.DyadWrapper;

import java.io.Serializable;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@Experimental
public class HashcodeEquals<T> extends DyadWrapper<ToIntFunction<T>, BiPredicate<T, T>> implements Serializable {

    public static <T> HashcodeEquals<T> of(final ToIntFunction<T> hashcode, final BiPredicate<T, T> equals) {
        return new HashcodeEquals<>(hashcode, equals);
    }

    /**
     * Uses the supplied function for both hashcode and equals.
     */
    public static <T> HashcodeEquals<T> ofIntFunction(final ToIntFunction<T> function) {
        return new HashcodeEquals<>(function, (o1, o2) -> function.applyAsInt(o1) == function.applyAsInt(o2));
    }

    /**
     * Uses the supplied function for both hashcode and equals.
     */
    public static <T> HashcodeEquals<T> ofLongFunction(final ToLongFunction<T> function) {
        return new HashcodeEquals<>(o -> (int) function.applyAsLong(o), (o1, o2) -> function.applyAsLong(o1) == function.applyAsLong(o2));
    }

    /**
     * Uses only the objects hashcode function to determine equality (the equals method is replaced by
     * hashcode == hashcode).
     */
    public static <T> HashcodeEquals<T> ofHashcode() {
        return new HashcodeEquals<>(Object::hashCode, (o1, o2) -> o1.hashCode() == o2.hashCode());
    }

    public static <T> HashcodeEquals<T> ofObject() {
        return new HashcodeEquals<>(Object::hashCode, Object::equals);
    }

    private HashcodeEquals(final ToIntFunction<T> hashcode, final BiPredicate<T, T> equals) {
        super(hashcode, equals);
    }

    public ToIntFunction<T> hashcodeFunction() {
        return rawFirst();
    }

    public BiPredicate<T, T> equalsFunction() {
        return rawSecond();
    }

    public HashcodeEqualsDecorator<T> decorate(final T value) {
        return HashcodeEqualsDecorator.of(value, this);
    }

}
