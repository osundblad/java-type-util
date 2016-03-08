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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValidationBehaviorThrowAfterTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void afterValidation_valid() {
        assertThat(LimitedString.init(new ValidationBehaviorThrowAfter()).length(5).matches("[0-9]+").build().of("12345"), is("12345"));
    }

    @Test
    public void afterValidation_twoErrors() {
        exception.expect(ValidationExceptionMatcher.of(2,
                "Length violation: 'abcdef' is longer (6) than the maximum length of 5.",
                "'abcdef' does not match [0-9]+"));
        LimitedString.init(new ValidationBehaviorThrowAfter()).length(5).matches("[0-9]+").build().of("abcdef");
    }

}