package dev.zemco.codegame.execution.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
class VerifyingInputSourceToOutputSinkAdapterTest {

    private IInputSource inputSource;
    private VerifyingInputSourceToOutputSinkAdapter adapter;

    @BeforeEach
    public void setUp() {
        this.inputSource = mock(IInputSource.class);
        this.adapter = new VerifyingInputSourceToOutputSinkAdapter(this.inputSource);
    }

    @Test
    void constructorShouldThrowIfInputSourceNull() {
        assertThrows(IllegalArgumentException.class, () -> new VerifyingInputSourceToOutputSinkAdapter(null));
    }

    @Test
    void acceptShouldThrowNotAcceptedExceptionIfInputSourceHasNoNextValue() {
        when(this.inputSource.hasNextValue()).thenReturn(false);
        assertThrows(NotAcceptedException.class, () -> this.adapter.accept(42));
    }

    @Test
    void acceptShouldThrowNotAcceptedExceptionIfValueFromInputSourceDiffersFromPassedValue() {
        when(this.inputSource.hasNextValue()).thenReturn(true);
        when(this.inputSource.getNextValue()).thenReturn(10);
        assertThrows(NotAcceptedException.class, () -> this.adapter.accept(2));
    }

    @Test
    void acceptShouldAcceptPassedValueIfItMatchesValueFromInputSource() {
        when(this.inputSource.hasNextValue()).thenReturn(true);
        when(this.inputSource.getNextValue()).thenReturn(42);
        this.adapter.accept(42);
    }

    @Test
    void isSatisfiedShouldReturnTrueIfInputSourceHasNoNextValue() {
        when(this.inputSource.hasNextValue()).thenReturn(false);
        assertThat(this.adapter.isSatisfied(), is(true));
    }

    @Test
    void isSatisfiedShouldReturnFalseIfInputSourceHasNextValue() {
        when(this.inputSource.hasNextValue()).thenReturn(true);
        assertThat(this.adapter.isSatisfied(), is(false));
    }

}
