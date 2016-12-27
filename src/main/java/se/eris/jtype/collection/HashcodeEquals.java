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
import se.eris.jtype.type.DyadWrapper;

import java.io.Serializable;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

@Experimental
public class HashcodeEquals<T> extends DyadWrapper<ToIntFunction<T>, BiPredicate<T, T>> implements Serializable {

    public static <T> HashcodeEquals<T> of(final ToIntFunction<T> hashcode, final BiPredicate<T, T> equals) {
        return new HashcodeEquals<T>(hashcode, equals);
    }

    public static <T> HashcodeEquals<T> of(final ToIntFunction<T> hashcode) {
        return new HashcodeEquals<T>(hashcode, (o1, o2) -> hashcode.applyAsInt(o1) == hashcode.applyAsInt(o2));
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

    public HashcodeEqualsDecorator<T> decorate(final T key) {
        return HashcodeEqualsDecorator.of(key, this);
    }

    @Override
    public String toString() {
        return "HashcodeEquals{" +
                rawFirst() + ", " +
                rawSecond() +
                "}";
    }

}
