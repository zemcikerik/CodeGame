package dev.zemco.codegame.util;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableValue;

import java.util.function.Function;

public final class BindingUtils {

    // prevents "walking with string", lacking JavaFX api
    // monadic
    public static <F> StringBinding mapOrNull(ObservableValue<F> property, Function<F, String> mapper) {
        // TODO: null checks
        return Bindings.createStringBinding(() -> {
            // TODO: maybe extract the not null part!
            F from = property.getValue();
            return from != null ? mapper.apply(from) : null;
        }, property);
    }

    private BindingUtils() {
        // prevent instantiation of this class
    }

}
