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
package se.eris.jtype.io;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.UUID;

/**
 * Some trivial methods to parse input Strings that might be null.
 * todo:
 *  - decide if empty string should behave as null
 */
@SuppressWarnings("WeakerAccess")
public final class IoString {

    private IoString() {
    }

    public static Optional<Double> toDouble(@Nullable final String s) {
        if (s == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Double.parseDouble(s));
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(s + " is not a Double", e);
        }
    }

    public static Optional<Integer> toInteger(@Nullable final String s) {
        if (s == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(s));
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(s + " is not an Integer", e);
        }
    }

    public static Optional<Short> toShort(@Nullable final String s) {
        if (s == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Short.parseShort(s));
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(s + " is not a Short", e);
        }
    }

    public static Optional<Boolean> toBoolean(@Nullable final String s) {
        if (s == null) {
            return Optional.empty();
        }
        if ("true".equalsIgnoreCase(s)) {
            return Optional.of(Boolean.TRUE);
        }
        if ("false".equalsIgnoreCase(s)) {
            return Optional.of(Boolean.FALSE);
        }
        throw new IllegalArgumentException(s + " is not a Boolean");
    }

    public static Optional<UUID> toUUID(@Nullable final String s) {
        if (s == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(s));
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException(s + " is not a UUID", e);
        }
    }

    public static Optional<LocalDate> toLocalDate(@Nullable final CharSequence s) {
        if (s == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalDate.parse(s));
        } catch (final DateTimeParseException e) {
            throw new IllegalArgumentException(s + " is not a LocalDate", e);
        }
    }

}
