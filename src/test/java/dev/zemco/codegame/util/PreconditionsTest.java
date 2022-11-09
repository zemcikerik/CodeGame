package dev.zemco.codegame.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PreconditionsTest {

    @Test
    public void checkArgumentNotNullShouldReturnArgumentIfNotNull() {
        Object argument = new Object();
        Object result = Preconditions.checkArgumentNotNull(argument, "Argument name");
        assertThat(result, sameInstance(argument));
    }

    @Test
    public void checkArgumentNotNullShouldThrowIllegalArgumentExceptionContainingArgumentNameIfArgumentIsNull() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Preconditions.checkArgumentNotNull(null, "Test")
        );
        assertThat(e.getMessage(), containsString("Test"));
    }

    @Test
    @SuppressWarnings("ConstantConditions") // for checking method output
    public void checkArgumentNotEmptyShouldReturnNullIfArgumentIsNull() {
        Object result = Preconditions.checkArgumentNotEmpty(null, "Argument name");
        assertThat(result, nullValue());
    }

    @Test
    public void checkArgumentNotEmptyShouldReturnArgumentIfNotEmpty() {
        String argument = "Hello, World!";
        String result = Preconditions.checkArgumentNotEmpty(argument, "Argument name");
        assertThat(result, is(argument));
    }

    @Test
    public void checkArgumentNotEmptyShouldThrowIllegalStateExceptionWithArgumentNameIfArgumentIsEmpty() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Preconditions.checkArgumentNotEmpty("", "Test")
        );
        assertThat(e.getMessage(), containsString("Test"));
    }

}
