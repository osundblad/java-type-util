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

/**
 * Useful for user entered floating point values, where you don't want float/double precision
 * change user input.
 */
public abstract class StringDoubleWrapper extends BasicWrapper<String> {

    protected StringDoubleWrapper(final String s) {
        super(s);
        //noinspection ResultOfMethodCallIgnored
        Double.parseDouble(s);
    }

    public Double asDouble() {
        return Double.parseDouble(raw());
    }

    public double asPrimitive() {
        return Double.parseDouble(raw());
    }

    public String asString() {
        return super.raw();
    }

}
