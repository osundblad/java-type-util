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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ValidationMessages {

    @NotNull
    public static ValidationMessages of(@NotNull final String message) {
        return ValidationMessages.of(ValidationMessage.of(message));
    }

    @NotNull
    public static ValidationMessages of(final List<ValidationMessage> messages) {
        return new ValidationMessages(messages);
    }

    @NotNull
    public static ValidationMessages of(@NotNull final ValidationMessage... messages) {
        return of(Arrays.asList(messages));
    }

    @NotNull
    public static ValidationMessages empty() {
        return of();
    }

    @NotNull
    private final List<ValidationMessage> messages;


    private ValidationMessages(@NotNull final List<ValidationMessage> messages) {
        this.messages = new ArrayList<>(messages);
    }

    public boolean hasMessages() {
        return !messages.isEmpty();
    }

    @NotNull
    public Stream<ValidationMessage> getMessages() {
        return messages.stream();
    }

}
