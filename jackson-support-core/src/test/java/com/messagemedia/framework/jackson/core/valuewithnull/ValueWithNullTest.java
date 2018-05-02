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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(DataProviderRunner.class)
public class ValueWithNullTest {

    private ObjectMapper objectMapper;

    public interface IMyClass<T> {
        ValueWithNull<T> getMyValue();

        void setMyValue(ValueWithNull<T> myValue);
    }

    public static class MyClass<T> implements IMyClass<T> {
        private ValueWithNull<T> myValue;

        public ValueWithNull<T> getMyValue() {
            return myValue;
        }

        public void setMyValue(ValueWithNull<T> myValue) {
            this.myValue = myValue;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MyClassNonNull<T> implements IMyClass<T> {
        private ValueWithNull<T> myValue;

        public ValueWithNull<T> getMyValue() {
            return myValue;
        }

        public void setMyValue(ValueWithNull<T> myValue) {
            this.myValue = myValue;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class MyClassNonEmpty<T> implements IMyClass<T> {
        private ValueWithNull<T> myValue;

        public ValueWithNull<T> getMyValue() {
            return myValue;
        }

        public void setMyValue(ValueWithNull<T> myValue) {
            this.myValue = myValue;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public static class MyClassNonAbsent<T> implements IMyClass<T> {
        private ValueWithNull<T> myValue;

        public ValueWithNull<T> getMyValue() {
            return myValue;
        }

        public void setMyValue(ValueWithNull<T> myValue) {
            this.myValue = myValue;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public static class MyClassNonDefault<T> implements IMyClass<T> {
        private ValueWithNull<T> myValue;

        public ValueWithNull<T> getMyValue() {
            return myValue;
        }

        public void setMyValue(ValueWithNull<T> myValue) {
            this.myValue = myValue;
        }
    }

    public static class MyClassConstructor<T> {
        private final ValueWithNull<T> myValue;

        public MyClassConstructor(@JsonProperty("myValue") ValueWithNull<T> myValue) {
            this.myValue = myValue;
        }

        public ValueWithNull<T> getMyValue() {
            return myValue;
        }
    }

    public static class MyValueClass {
        private final String value;

        public MyValueClass(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MyValueClass that = (MyValueClass) o;
            return Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    public static class MyComplexClass {
        private final String property;

        @JsonCreator
        public MyComplexClass(@JsonProperty("property") String property) {
            this.property = property;
        }

        public String getProperty() {
            return property;
        }

        @Override
        public String toString() {
            return property;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MyComplexClass that = (MyComplexClass) o;
            return Objects.equals(property, that.property);
        }

        @Override
        public int hashCode() {
            return Objects.hash(property);
        }
    }


    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testValueClass() throws IOException {
        MyClass<MyValueClass> myValueClass = new MyClass<>();
        myValueClass.setMyValue(ValueWithNull.of(new MyValueClass("myvalueclass")));
        assertEquals("{\"myValue\":\"myvalueclass\"}", objectMapper.writeValueAsString(myValueClass));

        TypeReference<MyClass<MyValueClass>> typeRef = new TypeReference<MyClass<MyValueClass>>() {
        };


        assertNull(((MyClass<MyValueClass>) objectMapper.readValue("{}", typeRef)).getMyValue());
        assertEquals(
                ValueWithNull.explicitNull(),
                ((MyClass<MyValueClass>) objectMapper.readValue("{\"myValue\":null}", typeRef)).getMyValue());

        assertEquals(
                myValueClass.getMyValue(), ((MyClass<MyValueClass>) objectMapper.readValue("{\"myValue\":\"myvalueclass\"}", typeRef)).getMyValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testComplexClass() throws IOException {
        MyClass<MyComplexClass> myValueClass = new MyClass<>();
        myValueClass.setMyValue(ValueWithNull.of(new MyComplexClass("mycomplexvalue")));
        assertEquals("{\"myValue\":{\"property\":\"mycomplexvalue\"}}", objectMapper.writeValueAsString(myValueClass));

        TypeReference<MyClass<MyComplexClass>> typeRef = new TypeReference<MyClass<MyComplexClass>>() {
        };


        assertNull(((MyClass<MyComplexClass>) objectMapper.readValue("{}", typeRef)).getMyValue());
        assertEquals(
                ValueWithNull.explicitNull(),
                ((MyClass<MyComplexClass>) objectMapper.readValue("{\"myValue\":null}", typeRef)).getMyValue());

        assertEquals(
                myValueClass.getMyValue(),
                ((MyClass<MyComplexClass>) objectMapper.readValue("{\"myValue\":{\"property\":\"mycomplexvalue\"}}", typeRef)).getMyValue());
    }

    @DataProvider
    public static List<List<Object>> data() {
        List<List<Object>> result = new ArrayList<>();
        for (List<Object> each : ImmutableList.<List<Object>>of(
                ImmutableList.of(10, "10"),
                ImmutableList.of("hrhr", "\"hrhr\"")
        )) {
            for (Supplier<IMyClass<?>> clazz : ImmutableList.<Supplier<IMyClass<?>>>of(
                    MyClass::new, MyClassNonNull::new, MyClassNonDefault::new, MyClassNonAbsent::new, MyClassNonEmpty::new)) {

                List<Object> params = new ArrayList<>(3);
                params.add(clazz);
                params.addAll(each);
                result.add(params);
            }

        }
        return result;
    }

    @Test
    @UseDataProvider("data")
    public <T> void serialise(Supplier<IMyClass<T>> create, T sampleValue, String jsonString) throws
            JsonProcessingException {
        assertEquals("{}", objectMapper.writeValueAsString(create.get()));
        IMyClass<T> explicitNull = create.get();
        explicitNull.setMyValue(ValueWithNull.explicitNull());
        assertEquals("{\"myValue\":null}", objectMapper.writeValueAsString(explicitNull));

        IMyClass<T> realValue = create.get();
        realValue.setMyValue(ValueWithNull.of(sampleValue));
        assertEquals(String.format("{\"myValue\":%s}", jsonString), objectMapper.writeValueAsString(realValue));
    }

    @Test
    @UseDataProvider("data")
    public <T> void deserialise(Supplier<IMyClass<T>> create, T sampleValue, String jsonString) throws IOException {
        @SuppressWarnings("unchecked")
        Class<? extends IMyClass<T>> myClass = (Class<? extends IMyClass<T>>) create.get().getClass();

        assertNull(objectMapper.readValue("{}", myClass).getMyValue());
        assertEquals(ValueWithNull.explicitNull(), objectMapper.readValue("{\"myValue\":null}", myClass).getMyValue());
        assertEquals(ValueWithNull.of(sampleValue), objectMapper.readValue(String.format("{\"myValue\":%s}", jsonString), myClass).getMyValue());
    }

    @DataProvider
    public static Object[][] dataConstructorInjection() {
        return new Object[][]{
                {10, "10"},
                {"hrhr", "\"hrhr\""},
        };
    }

    @UseDataProvider("dataConstructorInjection")
    @Test
    public <T> void constructorInjection(T sampleValue, String jsonString) throws IOException {
        MyClassConstructor<T> myClassConstructor = new MyClassConstructor<T>(null);
        assertEquals("{}", objectMapper.writeValueAsString(myClassConstructor));

        MyClassConstructor<T> myClassConstructorWithValue = new MyClassConstructor<T>(ValueWithNull.of(sampleValue));
        assertEquals(String.format("{\"myValue\":%s}", jsonString), objectMapper.writeValueAsString(myClassConstructorWithValue));

        assertEquals("{\"myValue\":null}", objectMapper.writeValueAsString(new MyClassConstructor<>(ValueWithNull.explicitNull())));


        assertEquals("{}", objectMapper.writeValueAsString(myClassConstructor));

        assertNull(objectMapper.readValue("{}", MyClassConstructor.class).getMyValue());
        assertEquals(sampleValue, objectMapper.readValue(String.format("{\"myValue\":%s}", jsonString), MyClassConstructor.class).getMyValue().get());
        assertEquals(ValueWithNull.explicitNull(), objectMapper.readValue("{\"myValue\":null}", MyClassConstructor.class).getMyValue());
    }

    @Test
    public void testToString() {
        assertEquals("explicitNull", ValueWithNull.explicitNull().toString());
        assertEquals("hrhr", ValueWithNull.of("hrhr").toString());
    }

    @Test
    public void canonical() {
        ValueWithNull<String> original = ValueWithNull.of("hrhr");
        ValueWithNull<String> dup = ValueWithNull.of("hrhr");
        ValueWithNull<String> other = ValueWithNull.of("hehe");

        assertCanonical(original, dup, other);
    }

    private void assertCanonical(Object instance, Object duplicate, Object other) {
        assertToString(instance);
        Object nullInstance = null;

        assertThat(instance, not(is(nullValue())));
        assertThat(duplicate, not(is(nullValue())));
        assertThat(other, not(is(nullValue())));

        assertThat(instance, is(instance));
        assertThat(instance, is(duplicate));
        assertThat(instance.equals(other), is(false));
        assertThat(instance.equals(nullInstance), is(false));
        assertThat(instance.hashCode(), is(duplicate.hashCode()));
    }

    private void assertToString(Object instance) {
        final String defaultToString = instance.getClass().getName() + "@[0-9a-fA-F]*";
        final String foundToString = instance.toString();
        final boolean matches = Pattern.matches(defaultToString, foundToString);
        assertFalse(String.format("Default toString detected [%s]", foundToString), matches);
    }


}

