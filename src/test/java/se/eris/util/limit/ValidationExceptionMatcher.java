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

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class ValidationExceptionMatcher extends TypeSafeDiagnosingMatcher<ValidationException> {

    public static ValidationExceptionMatcher of(@NotNull final String... contains) {
        return new ValidationExceptionMatcher(contains);
    }

    @NotNull
    private final String[] contains;

    private ValidationExceptionMatcher(@NotNull final String... contains) {
        this.contains = contains;
    }

    @Override
    protected boolean matchesSafely(@Nullable final ValidationException item, @NotNull final Description mismatchDescription) {
        if (item == null) {
            return false;
        }
        for (final String s : contains) {
            if (!item.getErrors().getErrors().anyMatch(e -> e.asString().contains(s))) {
                mismatchDescription.appendText(" does not contain ").appendValue("'" + s + "'");
                return false;
            }
        }
        return true;
    }

    @Override
    public void describeTo(@NotNull final Description description) {
        description.appendText("should throw " + ValidationException.class.getSimpleName());
        if (contains.length != 0) {
            description.appendValueList(", containing errors with all of the following strings: ", ", ", ".", Arrays.asList(contains));
        }
    }


}
