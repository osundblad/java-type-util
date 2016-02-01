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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationBehaviorThrowAfter implements ValidationBehavior {

    @NotNull
    private final List<ValidationError> validationErrors = new ArrayList<>();

    @NotNull
    @Override
    public ValidationBehavior instance() {
        return new ValidationBehaviorThrowAfter();
    }

    @Override
    public void atValidation(final @NotNull ValidationErrors messages) {
        if (messages.hasErrors()) {
            validationErrors.addAll(messages.getErrors().collect(Collectors.toList()));
        }
    }

    @Override
    public void afterValidation() {
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(ValidationErrors.of(validationErrors));
        }
    }


}
