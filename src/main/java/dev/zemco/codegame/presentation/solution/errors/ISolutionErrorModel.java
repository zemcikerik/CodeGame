package dev.zemco.codegame.presentation.solution.errors;

import java.util.Optional;

/**
 * Holds details about an error in user's solution to a {@link dev.zemco.codegame.problems.Problem problem}.
 * @author Erik Zemčík
 */
public interface ISolutionErrorModel {

    /**
     * Returns the description of the error.
     * @return description of the error
     */
    String getDescription();

    /**
     * Returns the zero-based line position within the solution code associated with the error.
     * This method may return an empty {@link Optional optional} when no line position is available.
     *
     * @return zero-based line position
     */
    Optional<Integer> getLinePosition();

}
