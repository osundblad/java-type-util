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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StringEmailLimitTest {

    @Test
    public void validate_ok() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("test@example.org").hasErrors(), is(false));
        assertThat(validator.validate("test..something@example.org").hasErrors(), is(false));
        assertThat(validator.validate("test._%+-something@example.org").hasErrors(), is(false));
    }

    @Test
    public void validate_multipleSubDomains() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("test@subone.subtwo.subthree.example.org").hasErrors(), is(false));
    }

    @Test
    public void validate_onlyTopDomain_shouldFail() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("test@example").hasErrors(), is(true));
    }

    @Test
    public void validate_onlyDomain_shouldFail() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("@example.org").hasErrors(), is(true));
    }

    @Test
    public void validate_missingDomain_shouldFail() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("test@").hasErrors(), is(true));
    }

    @Test
    public void validate_noAt_shouldFail() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("test.example.org").hasErrors(), is(true));
    }

    @Test
    public void validate_twoAt_shouldFail() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("first@second@example.org").hasErrors(), is(true));
    }

    @Test
    public void validate_twoConsecutiveDots_shouldFail() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("test@example..org").hasErrors(), is(true));
    }

    @Test
    public void validate_illegalCharacter_shouldFail() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("£euro@example.org").hasErrors(), is(true));
    }

}