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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LimitedIntegerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void of() {
        assertThat(LimitedInteger.init().range(2, 5).build().of(2), is(2));
        assertThat(LimitedInteger.init().range(2, 5).build().of(5), is(5));
    }

    @Test
    public void functionLimit_even() {
        final Limit<Integer> even = (item) -> (((item % 2) == 0) ? Optional.empty() : Optional.of(ValidationError.of(item + " is odd")));

        assertThat(LimitedInteger.init().add(even).build().of(2), is(2));

        exception.expect(ValidationExceptionMatcher.of(1, "1 is odd"));
        LimitedInteger.init().add(even).build().of(1);
    }

}