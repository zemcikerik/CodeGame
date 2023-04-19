package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.execution.IExecutionContext;

/**
 * Evaluates an underlying {@link IExecutionContext execution context} on set rules.
 * These rules typically match a specific {@link dev.zemco.codegame.problems.ProblemCase problem case}, which
 * solution for is being evaluated for.
 *
 * @author Erik Zemčík
 */
public interface ISolutionEvaluator {

    /**
     * Checks if the evaluator has finished its evaluation.
     * @return true if evaluator has finished, else false
     */
    boolean hasFinished();

    /**
     * Checks if the evaluation of the {@link IExecutionContext execution context} is successful.
     * Result of this method should only be considered if the evaluator {@link #hasFinished() has finished}
     * its evaluation.
     *
     * @return true if the evaluation has ended and is successful, else false
     */
    boolean isSuccessful();

    /**
     * Returns the {@link IExecutionContext execution context} that is being evaluated by the evaluator.
     * @return evaluated execution context
     */
    IExecutionContext getExecutionContext();

    /**
     * Steps the underlying execution and checks if the new state satisfies the rules being evaluated.
     * If the underlying execution throws an exception, the evaluation is considered finished as a failure
     * and the {@link StepEvaluationException} is thrown with the details of the failure.
     *
     * @throws StepEvaluationException if underlying execution threw an exception during the step
     * @throws IllegalStateException if step was attempted when evaluation has already finished
     */
    void step();

}
