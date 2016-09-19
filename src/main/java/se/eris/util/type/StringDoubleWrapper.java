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

/**
 * Useful for user entered floating point values, where you don't want rounding to show.
 */
public abstract class StringDoubleWrapper extends BasicWrapper<String> {

    protected StringDoubleWrapper(@NotNull final String s) {
        super(s);
        Double.parseDouble(s);
    }

    @NotNull
    public String asString() {
        return super.raw();
    }

    @NotNull
    public Double asDouble() {
        return Double.parseDouble(raw());
    }

    public double asPrimitive() {
        return Double.parseDouble(raw());
    }

}
