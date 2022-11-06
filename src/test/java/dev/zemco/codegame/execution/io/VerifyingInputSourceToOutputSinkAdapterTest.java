package dev.zemco.codegame.execution.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerifyingInputSourceToOutputSinkAdapterTest {

    @Mock
    private InputSource inputSource;

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowIfInputSourceNull() {
        new VerifyingInputSourceToOutputSinkAdapter(null);
    }

    @Test
    public void isSatisfiedShouldReturnTrueIfInputSourceHasNoNextValue() {
        when(this.inputSource.hasNextValue()).thenReturn(false);
        VerifyingInputSourceToOutputSinkAdapter adapter = new VerifyingInputSourceToOutputSinkAdapter(this.inputSource);
        assertThat(adapter.isSatisfied(), is(true));
    }

    @Test
    public void isSatisfiedShouldReturnFalseIfInputSourceHasNextValue() {
        when(this.inputSource.hasNextValue()).thenReturn(true);
        VerifyingInputSourceToOutputSinkAdapter adapter = new VerifyingInputSourceToOutputSinkAdapter(this.inputSource);
        assertThat(adapter.isSatisfied(), is(false));
    }

}
