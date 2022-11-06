package dev.zemco.codegame.execution.io;

import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IterableInputSourceTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowIllegalArgumentExceptionIfIterableNull() {
        new IterableInputSource(null);
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

    @Test(expected = NoSuchElementException.class)
    public void getNextValueShouldThrowNoSuchElementExceptionIfHasNoNextValue() {
        new IterableInputSource(emptyList()).getNextValue();
    }

}
