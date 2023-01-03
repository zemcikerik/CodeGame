package dev.zemco.codegame.execution.engine;

/**
 * Thrown when a jump is attempted, but the jump label is unknown to the engine.
 * @author Erik Zemčík
 */
public class UnknownJumpLabelException extends RuntimeException {

    /**
     * Creates an instance of {@link UnknownJumpLabelException}.
     * @param message detail message containing information about the state of the execution
     */
    public UnknownJumpLabelException(String message) {
        super(message);
    }

}
