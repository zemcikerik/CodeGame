package dev.zemco.codegame.util;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableIntegerValue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@Tag(UNIT_TEST)
class BindingUtilsTest {

    @Test
    void mapShouldThrowIllegalArgumentExceptionIfPropertyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> BindingUtils.map(null, ignored -> ""));
    }

    @Test
    void mapShouldThrowIllegalArgumentExceptionIfMapperIsNull() {
        ObservableIntegerValue property = new ReadOnlyIntegerWrapper().getReadOnlyProperty();
        assertThrows(IllegalArgumentException.class, () -> BindingUtils.map(property, null));
    }

    @Test
    void mapShouldBindPropertyValueToStringUsingMapper() {
        ReadOnlyIntegerWrapper propertyWrapper = new ReadOnlyIntegerWrapper(42);
        Function<Number, String> mapper = String::valueOf;

        StringBinding result = BindingUtils.map(propertyWrapper.getReadOnlyProperty(), mapper);

        assertThat(result.getValue(), equalTo("42"));
        propertyWrapper.set(-123);
        assertThat(result.getValue(), equalTo("-123"));
    }

    @Test
    void mapOrDefaultShouldThrowIllegalArgumentExceptionIfPropertyIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> BindingUtils.mapOrDefault(null, ignored -> "", "default")
        );
    }

    @Test
    void mapOrDefaultShouldThrowIllegalArgumentExceptionIfMapperIsNull() {
        ObservableIntegerValue property = new ReadOnlyIntegerWrapper().getReadOnlyProperty();
        assertThrows(
            IllegalArgumentException.class,
            () -> BindingUtils.mapOrDefault(property, null, "default")
        );
    }

    @Test
    void mapOrDefaultShouldBindPropertyValueToStringUsingMapper() {
        ReadOnlyObjectWrapper<String> propertyWrapper = new ReadOnlyObjectWrapper<>(" test");
        Function<String, String> mapper = String::trim;

        StringBinding result = BindingUtils.mapOrDefault(propertyWrapper.getReadOnlyProperty(), mapper, "default");

        assertThat(result.getValue(), equalTo("test"));
    }

    @Test
    void mapOrDefaultShouldUseDefaultValueIfPropertyValueIsNull() {
        ReadOnlyObjectWrapper<String> propertyWrapper = new ReadOnlyObjectWrapper<>(null);
        Function<String, String> mapper = ignored -> fail("Mapper was invoked!");

        StringBinding result = BindingUtils.mapOrDefault(propertyWrapper.getReadOnlyProperty(), mapper, "default");

        assertThat(result.getValue(), equalTo("default"));
    }

    @Test
    void mapOrNullShouldThrowIllegalArgumentExceptionIfPropertyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> BindingUtils.mapOrNull(null, ignored -> ""));
    }

    @Test
    void mapOrNullShouldThrowIllegalArgumentExceptionIfMapperIsNull() {
        ObservableIntegerValue property = new ReadOnlyIntegerWrapper().getReadOnlyProperty();
        assertThrows(IllegalArgumentException.class, () -> BindingUtils.mapOrNull(property, null));
    }

    @Test
    void mapOrNullShouldBindPropertyValueToStringUsingMapper() {
        ReadOnlyObjectWrapper<String> propertyWrapper = new ReadOnlyObjectWrapper<>("test ");
        Function<String, String> mapper = String::trim;

        StringBinding result = BindingUtils.mapOrNull(propertyWrapper.getReadOnlyProperty(), mapper);

        assertThat(result.getValue(), equalTo("test"));
    }

    @Test
    void mapOrNullShouldUseNullValueIfPropertyValueIsNull() {
        ReadOnlyObjectWrapper<String> propertyWrapper = new ReadOnlyObjectWrapper<>(null);
        Function<String, String> mapper = ignored -> fail("Mapper was invoked!");

        StringBinding result = BindingUtils.mapOrNull(propertyWrapper.getReadOnlyProperty(), mapper);

        assertThat(result.getValue(), nullValue());
    }

}
