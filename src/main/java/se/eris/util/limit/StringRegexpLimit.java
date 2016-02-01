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
package se.eris.util.limit;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class StringRegexpLimit implements StringLimit {

    @NotNull
    public static StringRegexpLimit of(@NotNull final Pattern pattern) {
        return new StringRegexpLimit(pattern);
    }

    @NotNull
    public static StringRegexpLimit of(@NotNull final String regexp) {
        return of(Pattern.compile(regexp));
    }

    @NotNull
    private final Pattern pattern;

    private StringRegexpLimit(@NotNull final Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public @NotNull ValidationErrors validate(@NotNull final String s) {
        if (!pattern.matcher(s).matches()) {
            return ValidationErrors.of("'" + s + "' does not match " + pattern);
        }
        return ValidationErrors.empty();
    }

}
