package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.execution.IExecutionContext;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

/**
 * Enforces maximum step count for a given evaluation.
 * If maximum number of steps is surpassed, the evaluation is considered finished unsuccessfully
 * and {@link TimeoutException} is thrown.
 *
 * @author Erik Zemčík
 */
public class TimeoutSolutionEvaluatorDecorator implements ISolutionEvaluator {

    private final ISolutionEvaluator wrappedEvaluator;
    private final int maxStepCount;
    private int currentStepCount;

    /**
     * Creates an instance of {@link TimeoutSolutionEvaluatorDecorator} which wraps an existing
     * {@link ISolutionEvaluator evaluator}.
     *
     * @param wrappedEvaluator evaluator to decorate
     * @param maxStepCount maximum allowed number of steps to be enforced
     *
     * @throws IllegalArgumentException if {@code wrappedEvaluator} is {@code null} or
     *                                  if {@code maxStepCount} is a negative integer
     */
    public TimeoutSolutionEvaluatorDecorator(ISolutionEvaluator wrappedEvaluator, int maxStepCount) {
        this.wrappedEvaluator = checkArgumentNotNull(wrappedEvaluator, "Wrapped evaluator");
        this.maxStepCount = checkArgumentPositiveInteger(maxStepCount, "Max step count");
        this.currentStepCount = 0;
    }

    @Override
    public boolean hasFinished() {
        return this.wrappedEvaluator.hasFinished() || this.currentStepCount > this.maxStepCount;
    }

    @Override
    public boolean isSuccessful() {
        return this.wrappedEvaluator.isSuccessful() && this.currentStepCount <= this.maxStepCount;
    }

    @Override
    public IExecutionContext getExecutionContext() {
        return this.wrappedEvaluator.getExecutionContext();
    }

    /**
     * Steps the decorated evaluator if the maximum allowed number of steps was not reached.
     * If the maximum number of steps was reached, the evaluation is considered finished as a failure
     * and the {@link TimeoutException} is thrown with the details of the failure.
     *
     * @throws TimeoutException if maximum allowed number of steps was reached
     * @throws StepEvaluationException if underlying execution threw an exception during the step
     * @throws IllegalStateException if step was attempted when evaluation has already finished
     *
     * @see ISolutionEvaluator#step()
     */
    @Override
    public void step() {
        if (++this.currentStepCount > this.maxStepCount) {
            throw new TimeoutException("Maximum number of steps reached!");
        }
        this.wrappedEvaluator.step();
    }

}
