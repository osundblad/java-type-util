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

        assertThat(validator.validate("olle@eris.se").hasMessages(), is(false));
        assertThat(validator.validate("olle.sundblad@eris.se").hasMessages(), is(false));
        assertThat(validator.validate("olle@sub.eris.se").hasMessages(), is(false));
        assertThat(validator.validate("olle@sub.sub.eris.se").hasMessages(), is(false));
    }

    @Test
    public void validate_onlyDomain() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("@eris.se").hasMessages(), is(true));
    }

    @Test
    public void validate_missingDomain() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("olle@").hasMessages(), is(true));
    }

    @Test
    public void validate_noAt() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("olle.eris.se").hasMessages(), is(true));
    }

    @Test
    public void validate_onlyTopDomain() {
        final StringEmailLimit validator = StringEmailLimit.getInstance();

        assertThat(validator.validate("olle@eris").hasMessages(), is(true));
    }

}