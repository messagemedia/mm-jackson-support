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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Objects;
import java.util.Optional;

/**
 * This class can be used when you want to distinguish between an explicit null and no value given. This useful if you want to implement a patch
 * method.
 * <p>If a field with the type ValueWithNull is set to null, then you wont get a null written to the JSON. If you parse the JSON into a field of
 * the type ValueWithNull, then this field is null.</p>
 * <p>When you use explicit null, then null is written to the JSON. If you use this class to parse the explicit null in the JSON, it will have the
 * value explicitNull.</p>
 * <p>
 * NOTE: While this class also works with constructor injection, please avoid it if you can. It relies on Jackson internals and it is not
 * guaranteed that it will be supported in the future. And it makes the code nicer if you have a patch object. Instead of a constructor with a lot
 * of nulls you simply set one value.
 *
 * Example usage for parsing:
 * <pre>
 ValueWithNull<String> yourValueExplicitNull = ValueWithNull.explicitNull();
 ValueWithNull<String> yourValueNotSet = null;
 ValueWithNull<String> yourValueWithValue = ValueWithNull.of("hrhr");

 for (ValueWithNull<?> each : new ValueWithNull[]{yourValueExplicitNull, yourValueNotSet, yourValueWithValue}) {
    Optional.ofNullable(each).ifPresent(x -> System.out.println("Got the value " + x.get()));
 }

 prints

 Got the value null
 Got the value hrhr
 * </pre>
 *
 * In a bean it would look like this:
 * <pre>
 public class MyBean {
    {@literal @}JsonProperty("foobar")
     private ValueWithNull<String> foobar;
     ...
 }
 * </pre>
 *
 *
 * @param <T> - the type of the underlying value
 */
@JsonDeserialize(using = ValueWithNullDeserializer.class)
@JsonSerialize(using = ValueWithNullSerializer.class)
public final class ValueWithNull<T> {

    private static final ValueWithNull<?> EXPLICIT_NULL = new ValueWithNull<>(null);
    private static final String EXPLICIT_NULL_TO_STRING = "explicitNull";

    private final T value;

    private ValueWithNull(T value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public static <X> ValueWithNull<X> explicitNull() {
        return (ValueWithNull<X>) EXPLICIT_NULL;
    }

    /**
     * Creates a new instance of ValueWithNull
     *
     * @param value - the value, might be null
     * @param <X>   - the type of underlying value
     * @return a ValueWithNull, never null!
     */
    public static <X> ValueWithNull<X> of(X value) {
        if (value == null) {
            // no need to generate new instances...
            return explicitNull();
        } else {
            return new ValueWithNull<X>(value);
        }
    }

    /**
     * @return true if this value is equals to explicit null
     */
    public boolean isExplicitNull() {
        return value == null;
    }

    /**
     * @return the underlying value, might be null
     */
    public T get() {
        return value;
    }

    /**
     *
     * @return Optionally wrapped underlying value.
     */
    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValueWithNull<?> that = (ValueWithNull<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        if (isExplicitNull()) {
            // this makes it easier to distinguish between null and explicit null
            return EXPLICIT_NULL_TO_STRING;
        } else {
            return Objects.toString(value);
        }
    }
}
