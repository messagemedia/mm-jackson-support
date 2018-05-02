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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;

public class ValueWithNullDeserializer extends JsonDeserializer<ValueWithNull<?>> implements ContextualDeserializer {

    private final Class<?> valueType;

    private ValueWithNullDeserializer(Class<?> valueType) {
        // instances built by this constructor will be later used to deserialise the value
        this.valueType = valueType;
    }

    public ValueWithNullDeserializer() {
        // This constructor is used in order to call createContextual later
        valueType = null;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        JavaType wrapperType = property.getType();

        final Class<?> clazz = wrapperType.containedType(0).getRawClass();
        return new ValueWithNullDeserializer(clazz);
    }

    @Override
    public ValueWithNull<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return ValueWithNull.of(jp.readValueAs(valueType));
    }

    @Override
    public ValueWithNull<?> getNullValue(DeserializationContext ctxt) {
        // This method is used when null is found in the actual JSON but also when the default value has to be calculated for constructor injection.
        // If there is no explicit null, we want to return null in order to allow to distinguish between explicit null and not given.
        if (ctxt.getParser().getCurrentToken() == JsonToken.VALUE_NULL) {
            // found an explicit null in the JSON, let's preserve it
            return ValueWithNull.explicitNull();
        } else {
            // value not given, let's use Java null for that.
            return null;
        }
    }
}
