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
package se.eris.jtype;

import org.jetbrains.annotations.NotNull;

public class StringTestUtil {

    @NotNull
    public static String createLongString(final int length) {
        final String str = "some more text again, ";
        final StringBuilder sb = new StringBuilder(length + str.length());
        while (sb.length() <= length) {
            sb.append(str);
        }
        return sb.toString().substring(0, length);
    }
}
