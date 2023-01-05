package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.io.IOutputSink;
import dev.zemco.codegame.problems.ProblemCase;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Implementation of the {@link IEvaluationStrategy evaluation strategy} that checks if the
 * {@link IOutputSink output sink} in the {@link IExecutionContext execution context} is satisfied.
 * This relies on the {@link IOutputSink output sink} being satisfied only if all valid values were emitted
 * during the execution.
 *
 * @author Erik Zemčík
 * @see IOutputSink#isSatisfied()
 */
public class OutputSinkSatisfiedEvaluationStrategy implements IEvaluationStrategy {

    /**
     * Evaluates the solution by checking if the {@link IOutputSink output sink} of the execution is satisfied.
     *
     * @param executionContext execution context to evaluate
     * @param problemCase problem case to check against, in this implementation ignored
     * @return true if {@link IOutputSink output sink} of the execution is satisfied
     *
     * @throws IllegalArgumentException if {@code executionContext} is {@code null} or
     *                                  if {@code problemCase} is {@code null}
     */
    @Override
    public boolean evaluateSolutionForProblemCase(IExecutionContext executionContext, ProblemCase problemCase) {
        checkArgumentNotNull(executionContext, "Execution context");
        checkArgumentNotNull(problemCase, "Problem case");

        return executionContext.getOutputSink().isSatisfied();
    }

}
