package dev.zemco.codegame.presentation.solution.errors;

import java.util.Optional;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

/**
 * Immutable implementation holding details about an error in user's solution
 * to a {@link dev.zemco.codegame.problems.Problem problem}.
 *
 * @author Erik Zemčík
 */
public class ImmutableSolutionErrorModel implements ISolutionErrorModel {

    private final String description;
    private final Integer linePosition;

    /**
     * Creates an instance of {@link ImmutableSolutionErrorModel} holding the given
     * {@code description} and {@code linePosition}.
     *
     * @param description description of the error
     * @param linePosition zero-based line position associated with the error, may be {@code null}
     *
     * @throws IllegalArgumentException if {@code description} is {@code null} or
     *                                  if {@code linePosition} is not {@code null} and is not a positive integer
     */
    public ImmutableSolutionErrorModel(String description, Integer linePosition) {
        this.description = checkArgumentNotNull(description, "Description");

        if (linePosition != null) {
            checkArgumentPositiveInteger(linePosition, "Line position");
        }

        this.linePosition = linePosition;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Optional<Integer> getLinePosition() {
        return Optional.ofNullable(this.linePosition);
    }

}
