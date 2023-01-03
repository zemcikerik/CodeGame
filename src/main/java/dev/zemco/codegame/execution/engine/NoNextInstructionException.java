package dev.zemco.codegame.execution.engine;

/**
 * Thrown when a step is attempted, but engine has no next instruction.
 * @author Erik Zemčík
 */
public class NoNextInstructionException extends IndexOutOfBoundsException {

    /**
     * Creates an instance of {@link NoNextInstructionException}.
     * @param message detail message containing information about the state of the execution
     */
    public NoNextInstructionException(String message) {
        super(message);
    }

}
