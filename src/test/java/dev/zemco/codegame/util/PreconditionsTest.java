package dev.zemco.codegame.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag(UNIT_TEST)
class PreconditionsTest {

    @Test
    void checkArgumentNotNullShouldReturnArgumentIfNotNull() {
        Object argument = new Object();
        Object result = Preconditions.checkArgumentNotNull(argument, "Argument name");
        assertThat(result, sameInstance(argument));
    }

    @Test
    void checkArgumentNotNullShouldThrowIllegalArgumentExceptionContainingArgumentNameIfArgumentIsNull() {
        IllegalArgumentException e = assertThrows(
            IllegalArgumentException.class,
            () -> Preconditions.checkArgumentNotNull(null, "Test")
        );
        assertThat(e.getMessage(), containsString("Test"));
    }

    @Test
    void checkArgumentNotNullShouldThrowIllegalArgumentExceptionContainingDefaultArgumentName() {
        IllegalArgumentException e = assertThrows(
            IllegalArgumentException.class,
            () -> Preconditions.checkArgumentNotNull(null, null)
        );
        assertThat(e.getMessage(), containsString("Argument"));
    }

    @Test
    void checkArgumentNotEmptyStringShouldReturnArgumentIfNotEmpty() {
        String argument = "Hello, World!";
        String result = Preconditions.checkArgumentNotEmpty(argument, "Argument name");
        assertThat(result, is(argument));
    }

    @Test
    void checkArgumentNotEmptyStringShouldThrowIllegalArgumentExceptionIfArgumentIsNull() {
        IllegalArgumentException e = assertThrows(
            IllegalArgumentException.class,
            () -> Preconditions.checkArgumentNotEmpty((String)null, "Test")
        );
        assertThat(e.getMessage(), containsString("Test"));
    }

    @Test
    void checkArgumentNotEmptyStringShouldThrowIllegalStateExceptionWithArgumentNameIfArgumentIsEmpty() {
        IllegalArgumentException e = assertThrows(
            IllegalArgumentException.class,
            () -> Preconditions.checkArgumentNotEmpty("", "Test")
        );
        assertThat(e.getMessage(), containsString("Test"));
    }

    @Test
    void checkArgumentNotEmptyStringShouldThrowIllegalStateExceptionWithDefaultArgumentName() {
        IllegalArgumentException e = assertThrows(
            IllegalArgumentException.class,
            () -> Preconditions.checkArgumentNotEmpty("", null)
        );
        assertThat(e.getMessage(), containsString("Argument"));
    }

}
