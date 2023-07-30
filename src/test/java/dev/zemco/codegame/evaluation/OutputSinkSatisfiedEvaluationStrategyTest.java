package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.io.IOutputSink;
import dev.zemco.codegame.problems.ProblemCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
class OutputSinkSatisfiedEvaluationStrategyTest {

    @Mock
    private IExecutionContext executionContext;

    @Mock
    private IOutputSink outputSink;

    @Mock
    private ProblemCase problemCase;

    private OutputSinkSatisfiedEvaluationStrategy strategy;

    @BeforeEach
    public void setUp() {
        this.strategy = new OutputSinkSatisfiedEvaluationStrategy();
        lenient().when(this.executionContext.getOutputSink()).thenReturn(this.outputSink);
    }

    @Test
    void evaluateSolutionForProblemCaseShouldThrowIllegalArgumentExceptionIfAnyArgumentIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.strategy.evaluateSolutionForProblemCase(null, this.problemCase)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> this.strategy.evaluateSolutionForProblemCase(this.executionContext, null)
        );
    }

    @Test
    void evaluateSolutionForProblemCaseShouldReturnTrueIfOutputSinkIsSatisfied() {
        when(this.outputSink.isSatisfied()).thenReturn(true);
        boolean result = this.strategy.evaluateSolutionForProblemCase(this.executionContext, this.problemCase);
        assertThat(result, is(true));
    }

    @Test
    void evaluateSolutionForProblemCaseShouldReturnFalseIfOutputSinkIsNotSatisfied() {
        when(this.outputSink.isSatisfied()).thenReturn(false);
        boolean result = this.strategy.evaluateSolutionForProblemCase(this.executionContext, this.problemCase);
        assertThat(result, is(false));
    }

}
