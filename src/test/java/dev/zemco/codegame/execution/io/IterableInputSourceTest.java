package dev.zemco.codegame.execution.io;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag(UNIT_TEST)
public class IterableInputSourceTest {

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfIterableNull() {
        assertThrows(IllegalArgumentException.class, () -> new IterableInputSource(null));
    }

    @Test
    public void shouldHaveNoValueIfCreatedWithEmptyIterable() {
        IterableInputSource inputSource = new IterableInputSource(emptyList());
        assertThat(inputSource.hasNextValue(), is(false));
    }

    @Test
    public void shouldProvideInputValuesInOrder() {
        List<Integer> values = List.of(1, 2, 3);
        IterableInputSource inputSource = new IterableInputSource(values);

        assertThat(inputSource.hasNextValue(), is(true));
        assertThat(inputSource.getNextValue(), is(1));

        assertThat(inputSource.hasNextValue(), is(true));
        assertThat(inputSource.getNextValue(), is(2));

        assertThat(inputSource.hasNextValue(), is(true));
        assertThat(inputSource.getNextValue(), is(3));

        assertThat(inputSource.hasNextValue(), is(false));
    }

    @Test
    public void getNextValueShouldThrowNoSuchElementExceptionIfHasNoNextValue() {
        IterableInputSource inputSource = new IterableInputSource(emptyList());
        assertThrows(NoSuchElementException.class, inputSource::getNextValue);
    }

}
