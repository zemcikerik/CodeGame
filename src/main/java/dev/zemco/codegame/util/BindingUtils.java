package dev.zemco.codegame.util;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableValue;

import java.util.function.Function;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Collection of utility methods for simplifying manipulation of JavaFX bindings.
 * <p>
 * Currently, these methods only consist of helper mapping methods that allow us to transform
 * observable properties using mappers. This was done because recommended solution that JavaFX provides
 * is walking properties using {@link Bindings#selectString(Object, String...)}.
 * This, in our opinion, is error-prone to changes, and alternatives tend to be harder to read.
 * <p>
 * To combat this issue we wrap creation of new bindings with simple to use and read mapping interface.
 *
 * @author Erik Zemčík
 */
public final class BindingUtils {

    /**
     * Creates binding that maps property to {@link String} using a {@code mapper}.
     *
     * @param <F> describes type of {@code property} which is passed to {@code mapper}
     * @param property source of values that are used for mapping
     * @param mapper {@link Function} that transforms value from {@code property} to {@link String}
     * @return binding that computes {@link String} value using {@code mapper}
     *
     * @throws IllegalArgumentException if {@code property} or {@code mapper} is {@code null}
     *
     * @see #mapOrDefault(ObservableValue, Function, String)
     * @see #mapOrNull(ObservableValue, Function)
     */
    public static <F> StringBinding map(ObservableValue<F> property, Function<F, String> mapper) {
        checkArgumentNotNull(property, "Property");
        checkArgumentNotNull(mapper, "Mapper");

        return Bindings.createStringBinding(() -> {
            F from = property.getValue();
            return mapper.apply(from);
        }, property);
    }

    /**
     * Creates binding that maps property to {@link String} using a {@code mapper}, falling back
     * to {@code defaultValue} if property value is {@code null}.
     *
     * @param <F> describes type of {@code property} which is passed to {@code mapper}
     * @param property source of values that are used for mapping
     * @param mapper {@link Function} that transforms non-null value from {@code property} to {@link String}
     * @param defaultValue fallback value if property value is {@code null}
     * @return binding that computes {@link String} value using {@code mapper}
     *
     * @throws IllegalArgumentException if {@code property} or {@code mapper} is {@code null}
     *
     * @see #map(ObservableValue, Function)
     * @see #mapOrNull(ObservableValue, Function)
     */
    public static <F> StringBinding mapOrDefault(
        ObservableValue<F> property,
        Function<F, String> mapper,
        String defaultValue
    ) {
        checkArgumentNotNull(mapper, "Mapper");
        return BindingUtils.map(property, from -> from != null ? mapper.apply(from) : defaultValue);
    }

    /**
     * Creates binding that maps property to {@link String} using a {@code mapper}, falling back
     * to {@code null} if property value is {@code null}.
     *
     * @param <F> describes type of {@code property} which is passed to {@code mapper}
     * @param property source of values that are used for mapping
     * @param mapper {@link Function} that transforms non-null value from {@code property} to {@link String}
     * @return binding that computes {@link String} value using {@code mapper}
     *
     * @throws IllegalArgumentException if {@code property} or {@code mapper} is {@code null}
     *
     * @see #map(ObservableValue, Function)
     * @see #mapOrDefault(ObservableValue, Function, String)
     */
    public static <F> StringBinding mapOrNull(ObservableValue<F> property, Function<F, String> mapper) {
        return BindingUtils.mapOrDefault(property, mapper, null);
    }

    private BindingUtils() {
        // prevent instantiation of this class
    }

}
