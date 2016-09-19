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
package se.eris.jtype.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@SuppressWarnings("MagicNumber")
public class IoStringTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void toDouble() {
        assertThat(IoString.toDouble("1.7").get(), is(1.7));
        assertThat(IoString.toDouble("0").get(), is(0d));
        assertThat(IoString.toDouble("-0").get(), is(-0d));
        assertThat(IoString.toDouble("-17.4711").get(), is(-17.4711));
        assertTrue(IoString.toDouble("1.5E-2").isPresent());

        assertTrue(IoString.toDouble("NaN").isPresent());
        assertTrue(IoString.toDouble("Infinity").isPresent());

        assertFalse(IoString.toDouble(null).isPresent());
    }

    @Test
    public void toDouble_notDouble() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Double");
        IoString.toDouble("abc").isPresent();
    }


    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void toInteger() {
        assertThat(IoString.toInteger("1234567").get(), is(1234567));
        assertThat(IoString.toInteger("0").get(), is(0));
        assertThat(IoString.toInteger("-0").get(), is(0));
        assertThat(IoString.toInteger("-17").get(), is(-17));
        assertThat(IoString.toInteger("016").get(), is(16));

        assertFalse(IoString.toInteger(null).isPresent());
    }

    @Test
    public void toInteger_notInteger() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Integer");
        IoString.toInteger("0xF").isPresent();
    }

    @Test
    public void toInteger_outOfRange() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Integer");
        IoString.toInteger(String.valueOf(Long.MAX_VALUE)).isPresent();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void toShort() {
        assertThat(IoString.toShort("0").get(), is((short) 0));
        assertThat(IoString.toShort("-0").get(), is((short) 0));
        assertThat(IoString.toShort("-17").get(), is((short) -17));
        assertThat(IoString.toShort("016").get(), is((short) 16));

        assertFalse(IoString.toShort(null).isPresent());
    }

    @Test
    public void toShort_outOfRange() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Short");
        IoString.toShort(String.valueOf(Integer.MAX_VALUE)).isPresent();
    }

    @Test
    public void toShort_notShort() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Short");
        IoString.toShort("0xF").isPresent();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void toBoolean() {
        assertThat(IoString.toBoolean("true").get(), is(true));
        assertThat(IoString.toBoolean("TruE").get(), is(true));
        assertThat(IoString.toBoolean("fALSe").get(), is(false));
        assertThat(IoString.toBoolean(null).isPresent(), is(false));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void toBoolean_notBoolean() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Boolean");
        assertThat(IoString.toBoolean("maybe").isPresent(), is(false));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void toUUID_roundtrip() {
        final UUID uuid = UUID.randomUUID();
        assertThat(IoString.toUUID(uuid.toString()).get(), is(uuid));
    }

    @Test
    public void toUUID_notUUID() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("UUID");
        IoString.toUUID("abc").isPresent();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void toLocalDate_roundtrip() {
        final LocalDate now = LocalDate.now();
        assertThat(IoString.toLocalDate(now.toString()).get(), is(now));
    }

    @Test
    public void toLocalDate_null() {
        assertThat(IoString.toLocalDate(null), is(Optional.empty()));
    }

    @Test
    public void toLocalDate_notLocalDate() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("LocalDate");
        IoString.toLocalDate("abc").isPresent();
    }

}