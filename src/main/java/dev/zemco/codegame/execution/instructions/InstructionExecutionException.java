package dev.zemco.codegame.execution.instructions;

/**
 * Thrown when an illegal state during instruction execution is reached.
 * @author Erik Zemčík
 */
public class InstructionExecutionException extends Exception {

    /**
     * Creates an instance of {@link InstructionExecutionException}.
     * @param message detail message containing information about the instruction execution failure
     */
    public InstructionExecutionException(String message) {
        super(message);
    }

    /**
     * Creates an instance of {@link InstructionExecutionException}.
     * @param message detail message containing information about the instruction execution failure
     * @param cause cause of the instruction execution failure
     */
    public InstructionExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
