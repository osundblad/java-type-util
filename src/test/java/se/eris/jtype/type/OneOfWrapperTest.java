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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OneOfWrapperTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void new_firstAndNotSecond_shouldSucceed() {
        new Subject("Test", null);
    }

    @Test
    public void new_notFirstAndSecond_shouldSucceed() {
        new Subject(null, 7);
    }

    @Test
    public void new_firstAndSecond_shouldFail() {
        exception.expect(RuntimeException.class);
        exception.expectMessage("Both values cannot be present");
        new Subject("Test", 7);
    }

    @Test
    public void new_notFirstAndNotSecond_shouldFail() {
        exception.expect(RuntimeException.class);
        exception.expectMessage("Both values cannot be absent");
        new Subject(null, null);
    }

    @Test
    public void toString_shouldWork() {
        assertThat(new Subject("Test", null).toString(), is("Subject{Test, null}"));
    }

    private static class Subject extends OneOfWrapper<String, Integer> {
        Subject(@Nullable final String first, @Nullable final Integer second) {
            super(first, second);
        }
    }

}