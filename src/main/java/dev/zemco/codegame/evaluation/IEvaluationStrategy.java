package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.problems.ProblemCase;

/**
 * Algorithm that evaluates if the {@link IExecutionContext execution context} has reached a state,
 * where it satisfies rules as a valid solution of a {@link ProblemCase problem case}.
 *
 * @author Erik Zemčík
 */
public interface IEvaluationStrategy {

    /**
     * Evaluates if the {@link IExecutionContext execution context} is in a state that is considered
     * a valid solution of a given {@link ProblemCase problem case}.
     *
     * @param executionContext execution context to evaluate
     * @param problemCase problem case to check against
     * @return true if the state of the execution context is considered a valid solution, else false
     *
     * @throws IllegalArgumentException if {@code executionContext} is {@code null} or
     *                                  if {@code problemCase} is {@code null}
     */
    boolean evaluateSolutionForProblemCase(IExecutionContext executionContext, ProblemCase problemCase);

}
