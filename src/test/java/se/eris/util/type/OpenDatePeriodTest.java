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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class OpenDatePeriodTest {

    private static final int YEAR_2000 = 2000;
    private static final LocalDate DATE_2000_01_01 = LocalDate.of(YEAR_2000, 1, 1);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void between_valid() {
        OpenDatePeriod.between(DATE_2000_01_01, DATE_2000_01_01.plusYears(1));
    }

    @Test
    public void between_endDateBeforeStartDate_shouldFail() {
        exception.expect(IllegalArgumentException.class);
        OpenDatePeriod.between(DATE_2000_01_01.plusDays(1), LocalDate.of(YEAR_2000, 1, 1));
    }

    @Test
    public void isInPeriod() {
        assertThat(OpenDatePeriod.between(DATE_2000_01_01, DATE_2000_01_01.plusYears(1)).isInPeriod(DATE_2000_01_01.minusDays(1)), is(false));
        assertThat(OpenDatePeriod.between(DATE_2000_01_01, DATE_2000_01_01.plusYears(1)).isInPeriod(DATE_2000_01_01), is(true));
        assertThat(OpenDatePeriod.between(DATE_2000_01_01, DATE_2000_01_01.plusYears(1)).isInPeriod(DATE_2000_01_01.plusYears(1)), is(true));
        assertThat(OpenDatePeriod.between(DATE_2000_01_01, DATE_2000_01_01.plusYears(1)).isInPeriod(DATE_2000_01_01.plusYears(1).plusDays(1)), is(false));
    }

    @Test
    public void toString_() {
        final LocalDate startDate = LocalDate.parse("2015-01-01");
        final LocalDate endDate = LocalDate.parse("2015-12-31");

        assertThat(OpenDatePeriod.between(startDate, endDate).toString(), is("OpenDatePeriod{2015-01-01 - 2015-12-31}"));
        assertThat(OpenDatePeriod.from(startDate).toString(), is("OpenDatePeriod{2015-01-01 - }"));
        assertThat(OpenDatePeriod.to(endDate).toString(), is("OpenDatePeriod{ - 2015-12-31}"));
    }


}