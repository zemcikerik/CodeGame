package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.execution.IExecutionContext;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
public class TimeoutSolutionEvaluatorDecoratorTest {

    @Mock
    private ISolutionEvaluator solutionEvaluator;

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfWrappedEvaluatorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new TimeoutSolutionEvaluatorDecorator(null, 5));
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfMaxStepCountIsNegative() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new TimeoutSolutionEvaluatorDecorator(this.solutionEvaluator, -1)
        );
    }

    @Test
    public void evaluationShouldBeSuccessfulIfWrappedEvaluatorHasFinishedSuccessfully() {
        when(this.solutionEvaluator.hasFinished()).thenReturn(true);
        when(this.solutionEvaluator.isSuccessful()).thenReturn(true);
        var decorator = new TimeoutSolutionEvaluatorDecorator(this.solutionEvaluator, 100);

        assertThat(decorator.hasFinished(), is(true));
        assertThat(decorator.isSuccessful(), is(true));
    }

    @Test
    public void evaluationShouldNotBeFinishedIfWrappedEvaluatorHasNotFinished() {
        when(this.solutionEvaluator.hasFinished()).thenReturn(false);
        when(this.solutionEvaluator.isSuccessful()).thenReturn(false);
        var decorator = new TimeoutSolutionEvaluatorDecorator(this.solutionEvaluator, 100);

        assertThat(decorator.hasFinished(), is(false));
        assertThat(decorator.isSuccessful(), is(false));
    }

    @Test
    public void evaluationShouldFailIfMaxStepCountReached() {
        when(this.solutionEvaluator.hasFinished()).thenReturn(false);
        when(this.solutionEvaluator.isSuccessful()).thenReturn(false);
        doNothing().when(this.solutionEvaluator).step();
        var decorator = new TimeoutSolutionEvaluatorDecorator(this.solutionEvaluator, 2);

        decorator.step();
        decorator.step();
        assertThrows(TimeoutException.class, decorator::step);

        assertThat(decorator.hasFinished(), is(true));
        assertThat(decorator.isSuccessful(), is(false));
    }

    @Test
    public void getExecutionContextShouldReturnContextOfWrapperEvaluator() {
        IExecutionContext executionContext = mock(IExecutionContext.class);
        when(this.solutionEvaluator.getExecutionContext()).thenReturn(executionContext);
        var decorator = new TimeoutSolutionEvaluatorDecorator(this.solutionEvaluator, 100);

        IExecutionContext result = decorator.getExecutionContext();

        assertThat(result, is(executionContext));
    }

}
