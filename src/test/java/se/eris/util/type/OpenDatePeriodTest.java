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

public class OpenDatePeriodTest {

    private static final int YEAR_2000 = 2000;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void between_valid() {
        OpenDatePeriod.between(LocalDate.of(YEAR_2000, 1, 1), LocalDate.of(YEAR_2000 + 1, 1, 1));
    }

    @Test
    public void between_endDateBeforeStartDate_shouldFail() {
        exception.expect(IllegalArgumentException.class);
        OpenDatePeriod.between(LocalDate.of(YEAR_2000 + 1, 1, 1), LocalDate.of(YEAR_2000, 1, 1));
    }

}