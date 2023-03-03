package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.engine.IExecutionEngine;
import dev.zemco.codegame.execution.engine.NoNextInstructionException;
import dev.zemco.codegame.execution.engine.StepExecutionException;
import dev.zemco.codegame.problems.ProblemCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
public class SolutionEvaluatorTest {

    @Mock
    private IExecutionContext executionContext;

    @Mock
    private IExecutionEngine executionEngine;

    @Mock
    private IEvaluationStrategy evaluationStrategy;

    @Mock
    private ProblemCase problemCase;

    @InjectMocks
    private SolutionEvaluator solutionEvaluator;

    @BeforeEach
    public void setUp() {
        lenient().when(this.executionContext.getExecutionEngine()).thenReturn(this.executionEngine);
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfAnyArgumentIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new SolutionEvaluator(null, this.evaluationStrategy, this.problemCase)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new SolutionEvaluator(this.executionContext, null, this.problemCase)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new SolutionEvaluator(this.executionContext, this.evaluationStrategy, null)
        );
    }

    @Test
    public void evaluationShouldBeCompletedIfEvaluationStrategyAlreadySatisfiedDuringConstruction() {
        this.mockEvaluationStrategyResult(true);

        SolutionEvaluator evaluator = new SolutionEvaluator(this.executionContext, this.evaluationStrategy, this.problemCase);

        assertThat(evaluator.hasFinished(), is(true));
        assertThat(evaluator.isSuccessful(), is(true));
    }

    @Test
    public void stepShouldStepUnderlyingExecution() {
        this.mockEvaluationStrategyResult(false);
        this.solutionEvaluator.step();
        verify(this.executionEngine, times(1)).step();
    }

    @Test
    public void stepShouldFinishEvaluationSuccessfullyWhenEvaluationStrategyIsSatisfied() {
        this.mockEvaluationStrategyResult(true);

        this.solutionEvaluator.step();

        assertThat(this.solutionEvaluator.hasFinished(), is(true));
        assertThat(this.solutionEvaluator.isSuccessful(), is(true));
    }

    @Test
    public void stepShouldThrowStepEvaluationExceptionAndFinishEvaluationIfExecutionHasNoNextInstruction() {
        doThrow(NoNextInstructionException.class).when(this.executionEngine).step();
        assertThrows(StepEvaluationException.class, () -> this.solutionEvaluator.step());
        assertThat(this.solutionEvaluator.hasFinished(), is(true));
        assertThat(this.solutionEvaluator.isSuccessful(), is(false));
    }

    @Test
    public void stepShouldThrowStepEvaluationExceptionAndFinishEvaluationIfExecutionStepFails() {
        doThrow(StepExecutionException.class).when(this.executionEngine).step();
        assertThrows(StepEvaluationException.class, () -> this.solutionEvaluator.step());
        assertThat(this.solutionEvaluator.hasFinished(), is(true));
        assertThat(this.solutionEvaluator.isSuccessful(), is(false));
    }

    @Test
    public void stepShouldThrowIllegalStateExceptionIfEvaluationHasAlreadyFinished() {
        this.mockEvaluationStrategyResult(true);
        this.solutionEvaluator.step();

        assertThrows(IllegalStateException.class, () -> this.solutionEvaluator.step());
    }

    @Test
    public void getExecutionContextShouldReturnUnderlyingExecutionContext() {
        assertThat(this.solutionEvaluator.getExecutionContext(), equalTo(this.executionContext));
    }

    private void mockEvaluationStrategyResult(boolean result) {
        when(
            this.evaluationStrategy.evaluateSolutionForProblemCase(this.executionContext, this.problemCase)
        ).thenReturn(result);
    }

}
