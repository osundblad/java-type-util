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

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * This limit will validate most email addresses that are in use today.
 *
 * Note though, it does not follow the RFC 5322 specification.
 * Why this pattern see: http://www.regular-expressions.info/email.html
 */
public class StringEmailLimit implements StringLimit {

    /**
     * Why this pattern see: http://www.regular-expressions.info/email.html
     */
    @NotNull
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]++@(?:[A-Z0-9-]++\\.)++[A-Z]{2,}+$", Pattern.CASE_INSENSITIVE);

    private static final StringEmailLimit INSTANCE = new StringEmailLimit();

    @NotNull
    public static StringEmailLimit getInstance() {
        return INSTANCE;
    }

    @NotNull
    private final StringLimit emailLimit = StringRegexpLimit.of(EMAIL_PATTERN);

    @Override
    @NotNull
    public Optional<ValidationError> validate(@NotNull final String emailAddress) {
        if (emailLimit.validate(emailAddress).isPresent()) {
            return Optional.of(ValidationError.of("'" + emailAddress + "' is not a valid email address"));
        }
        return Optional.empty();
    }

}
