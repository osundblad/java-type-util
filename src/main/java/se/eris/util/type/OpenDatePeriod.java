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
package se.eris.util.type;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Optional;

/**
 * Inclusive date period [from - to]. Both from and to are optional missing value means no limit.
 */
public class OpenDatePeriod extends PairWrapper<Optional<LocalDate>> {

    @NotNull
    public static OpenDatePeriod of(@NotNull final Optional<LocalDate> startDate, @NotNull final Optional<LocalDate> endDate) {
        return new OpenDatePeriod(startDate, endDate);
    }

    @NotNull
    public static OpenDatePeriod from(@NotNull final LocalDate startDate) {
        return of(Optional.of(startDate), Optional.empty());
    }

    @NotNull
    public static OpenDatePeriod to(@NotNull final LocalDate endDate) {
        return new OpenDatePeriod(Optional.empty(), Optional.of(endDate));
    }

    @NotNull
    public static OpenDatePeriod between(@NotNull final LocalDate startDate, @NotNull final LocalDate endDate) {
        return of(Optional.of(startDate), Optional.of(endDate));
    }

    @NotNull
    public static final OpenDatePeriod ALWAYS = of(Optional.empty(), Optional.empty());

    private OpenDatePeriod(@NotNull final Optional<LocalDate> startDate, @NotNull final Optional<LocalDate> endDate) {
        super(startDate, endDate);
        validate();
    }

    private void validate() {
        if (isEndBeforeStart()) {
            throw new IllegalArgumentException(rawSecond() + " is before " + rawSecond());
        }
    }

    private boolean isEndBeforeStart() {
        return hasStart() && hasEnd() && rawSecond().get().isBefore(rawFirst().get());
    }

    public boolean hasStart() {
        return rawFirst().isPresent();
    }

    @NotNull
    public LocalDate getStartDate() {
        return rawFirst().get();
    }

    public boolean hasEnd() {
        return rawSecond().isPresent();
    }

    @NotNull
    public LocalDate getEndDate() {
        return rawSecond().get();
    }

    public boolean isInPeriod(@NotNull final ChronoLocalDate localDate) {
        if (hasStart() && localDate.isBefore(getStartDate())) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (hasEnd() && localDate.isAfter(getEndDate())) {
            return false;
        }
        return true;
    }

    @Override
    @NotNull
    public String toString() {
        final String from = hasStart() ? getStartDate().toString() : "";
        final String to = hasEnd() ? getEndDate().toString() : "";
        return "OpenDatePeriod{" + from + " - " + to + "}";
    }
}
