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
import se.eris.util.type.OpenDatePeriod;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StringDateLimit implements StringLimit {

    @NotNull
    public StringDateLimit iso() {
        return of(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @NotNull
    public StringDateLimit iso(@NotNull final OpenDatePeriod datePeriod) {
        return of(DateTimeFormatter.ISO_LOCAL_DATE, datePeriod);
    }

    @NotNull
    public static StringDateLimit of(@NotNull final DateTimeFormatter dateFormat) {
        return new StringDateLimit(dateFormat, OpenDatePeriod.ALWAYS);
    }

    @NotNull
    public static StringDateLimit of(@NotNull final DateTimeFormatter dateFormat, @NotNull final OpenDatePeriod datePeriod) {
        return new StringDateLimit(dateFormat, datePeriod);
    }

    @NotNull
    private final DateTimeFormatter dateFormat;

    @NotNull
    private final OpenDatePeriod datePeriod;

    private StringDateLimit(@NotNull final DateTimeFormatter dateFormat, @NotNull final OpenDatePeriod datePeriod) {
        this.dateFormat = dateFormat;
        this.datePeriod = datePeriod;
    }

    @NotNull
    @Override
    public ValidationMessages validate(@NotNull final String s) {
        final LocalDate localDate;
        try {
            localDate = LocalDate.parse(s, dateFormat);
        } catch (final DateTimeParseException e) {
            return ValidationMessages.of("'" + s + "' is not a valid date " + dateFormat.toFormat());
        }
        if (!datePeriod.isInPeriod(localDate)) {
            return ValidationMessages.of("'" + s + "' is not in date period " + datePeriod.toString());
        }
        return ValidationMessages.empty();
    }

}
