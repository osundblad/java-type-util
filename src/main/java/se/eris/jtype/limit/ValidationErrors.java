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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ValidationErrors {

    @NotNull
    public static ValidationErrors of(@NotNull final List<ValidationError> errors) {
        return new ValidationErrors(errors);
    }

    @NotNull
    public static ValidationErrors of(@NotNull final ValidationError error) {
        return of(Collections.singletonList(error));
    }

    @NotNull
    private final List<ValidationError> errors;

    private ValidationErrors(@NotNull final List<ValidationError> errors) {
        if (errors.isEmpty()) {
            throw new IllegalArgumentException("Cannot create " + ValidationErrors.class.getSimpleName() + " without errors");
        }
        this.errors = new ArrayList<>(errors);
    }

    @NotNull
    public Stream<ValidationError> stream() {
        return errors.stream();
    }

    @Override
    @NotNull
    public String toString() {
        return "ValidationErrors{errors=" + errors + '}';
    }

}
