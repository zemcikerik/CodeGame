package dev.zemco.codegame.execution.engine;

import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

/**
 * Thrown when an exception is thrown during instruction execution while engine is doing a step.
 * @author Erik Zemčík
 */
public class StepExecutionException extends RuntimeException {

    private final int linePosition;

    /**
     * Creates an instance of {@link StepExecutionException}.
     * @param message detail message containing information about the state of the execution
     * @param cause exception thrown by instruction during execution
     * @param linePosition index of the line that was executed
     * @throws IllegalArgumentException if {@code linePosition} is not a positive integer
     */
    public StepExecutionException(String message, Throwable cause, int linePosition) {
        super(message, cause);
        this.linePosition = checkArgumentPositiveInteger(linePosition, "Line position");
    }

    /**
     * Returns the zero-based line position related to the executed instruction.
     * @return zero-based line position
     */
    public int getLinePosition() {
        return this.linePosition;
    }
    
}
