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
package se.eris.jtype.limit;

import java.util.Optional;
import java.util.regex.Pattern;

public class StringRegexpLimit implements StringLimit {

    public static StringRegexpLimit of(final Pattern pattern) {
        return new StringRegexpLimit(pattern);
    }

    public static StringRegexpLimit of(final String regexp) {
        return of(Pattern.compile(regexp));
    }

    private final Pattern pattern;

    private StringRegexpLimit(final Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public Optional<ValidationError> validate(final String s) {
        if (!pattern.matcher(s).matches()) {
            return Optional.of(ValidationError.of("'" + s + "' does not match " + pattern));
        }
        return Optional.empty();
    }

}
