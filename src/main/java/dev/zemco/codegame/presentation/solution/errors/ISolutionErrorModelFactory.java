package dev.zemco.codegame.presentation.solution.errors;

import dev.zemco.codegame.compilation.InvalidSyntaxException;
import dev.zemco.codegame.evaluation.StepEvaluationException;

/**
 * Produces {@link ISolutionErrorModel solution error models} for exceptions relevant
 * to user's solutions.
 *
 * @author Erik Zemčík
 * @see ISolutionErrorModel
 */
public interface ISolutionErrorModelFactory {

    /**
     * Creates a {@link ISolutionErrorModel solution error model} for an exception
     * caused by usage of an invalid syntax in the solution.
     *
     * @param exception invalid syntax exception
     * @return model containing relevant information about the exception
     * @throws IllegalArgumentException if {@code exception} is {@code null}
     */
    ISolutionErrorModel createSolutionErrorModel(InvalidSyntaxException exception);

    /**
     * Creates a {@link ISolutionErrorModel solution error model} for an exception
     * caused by error during evaluation of the solution.
     *
     * @param exception step evaluation exception
     * @return model containing relevant information about the exception
     * @throws IllegalArgumentException if {@code exception} is {@code null}
     */
    ISolutionErrorModel createSolutionErrorModel(StepEvaluationException exception);

}
