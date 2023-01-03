package dev.zemco.codegame.evaluation;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Thrown during the evaluation step if the underlying execution throws an exception.
 * This exception is always guaranteed to have an underlying non-null cause accessible through {@link #getCause()}.
 *
 * @author Erik Zemčík
 */
public class StepEvaluationException extends RuntimeException {

    /**
     * Creates an instance of {@link StepEvaluationException}.
     *
     * @param message detail message containing information about the state of the evaluation
     * @param cause exception thrown by the execution
     *
     * @throws IllegalArgumentException if {@code cause} is {@code null}
     */
    public StepEvaluationException(String message, Throwable cause) {
        super(message, checkArgumentNotNull(cause, "Cause"));
    }

}
