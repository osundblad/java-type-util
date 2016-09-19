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
package se.eris.jtype.limit;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ValidationBehavior {

    /**
     * todo Needs a better name!
     * @see AbstractLimited#of(Object)
     * @return creates a new instance called at validation.
     */
    @NotNull
    ValidationBehavior instance();

    void atValidation(@NotNull Optional<ValidationError> error);

    void afterValidation();

}
