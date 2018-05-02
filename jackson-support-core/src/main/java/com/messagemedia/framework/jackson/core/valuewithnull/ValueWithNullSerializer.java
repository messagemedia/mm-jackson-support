/*
 * Copyright 2018 MessageMedia Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @file
 * @copyright 2018 MessageMedia Group
 * @license https://www.apache.org/licenses/LICENSE-2.0
 * @see https://messagemedia.github.io/
 *
 */

package com.messagemedia.framework.jackson.core.valuewithnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ValueWithNullSerializer extends JsonSerializer<ValueWithNull<?>> {

    @Override
    public void serialize(ValueWithNull<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value.isExplicitNull()) {
            // preserve null
            gen.writeNull();
        } else {
            gen.writeObject(value.get());
        }
    }

    @Override
    public boolean isEmpty(SerializerProvider provider, ValueWithNull<?> value) {
        // This method determines if the current instance is empty. Classical example would be an empty list. If that is the case,
        // then the value might be omitted (I guess!) from serialisation, depending on the config. When we got a value, we always want to write
        // it, even though we have explicit null.
        return false;
    }
}
