/*
 *    Copyright 2016 Olle Sundblad - olle@eris.se
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
package se.eris.jtype.type;

import org.jetbrains.annotations.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import se.eris.test.SerializeUtil;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class MaxOneOfWrapperTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void new_firstAndNotSecond_shouldSucceed() {
        new Subject("test", null);
    }

    @Test
    public void new_notFirstAndSecond_shouldSucceed() {
        new Subject(null, 7);
    }

    @Test
    public void new_notfirstAndNotSecond_shouldSucceed() {
        new Subject(null, null);
    }

    @Test
    public void new_firstAndSecond_shouldFail() {
        exception.expect(RuntimeException.class);
        exception.expectMessage("Both values cannot be present");
        new Subject("test", 7);
    }

    @Test
    public void equals() {
        final Subject testNull = new Subject("test", null);
        final Subject testNullNew = new Subject("test", null);
        final Subject nullNull = new Subject(null, null);
        final Subject otherNull = new Subject("other", null);
        final Subject nullSeven = new Subject(null, 7);
        final Subject nullEight = new Subject(null, 8);
        assertThat(testNull, is(testNull));
        assertThat(testNull, is(testNullNew));
        assertThat(testNull, not(otherNull));
        assertThat(nullSeven, not(nullEight));
        assertThat(nullSeven, not(nullNull));
        assertThat(testNull, not(nullNull));
    }

    @Test
    public void toString_shouldWork() {
        assertThat(new Subject("Test", null).toString(), is("Subject{Test, null}"));
    }

    @Test
    public void serializable_shouldBeSerializable() throws IOException, ClassNotFoundException {
        final Subject otherNull = new Subject("other", null);
        assertThat(SerializeUtil.roundtrip(otherNull, Subject.class), is(otherNull));
    }

    private static class Subject extends MaxOneOfWrapper<String, Integer> {
        Subject(@Nullable final String first, @Nullable final Integer second) {
            super(first, second);
        }

    }

}