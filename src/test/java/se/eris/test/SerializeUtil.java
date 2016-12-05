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
package se.eris.test;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class SerializeUtil {

    @NotNull
    public static <T extends Serializable> byte[] toBytes(final T obj) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ObjectOutput objectOutput = new ObjectOutputStream(outputStream)) {
            objectOutput.writeObject(obj);
            return outputStream.toByteArray();
        }
    }

    @NotNull
    public static <T extends Serializable> T fromBytes(final byte[] b, final Class<T> cl) throws IOException, ClassNotFoundException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(b);
        try (ObjectInput objectInput = new ObjectInputStream(inputStream)) {
            return cl.cast(objectInput.readObject());
        }
    }

    @NotNull
    public static <T extends Serializable> T roundtrip(@NotNull final T obj, @NotNull final Class<T> cl) throws IOException, ClassNotFoundException {
        return SerializeUtil.fromBytes(SerializeUtil.toBytes(obj), cl);
    }

}
