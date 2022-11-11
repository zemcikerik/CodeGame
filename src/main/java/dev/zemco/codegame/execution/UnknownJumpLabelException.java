package dev.zemco.codegame.execution;

public class UnknownJumpLabelException extends RuntimeException {

    public UnknownJumpLabelException() {
    }

    public UnknownJumpLabelException(String message) {
        super(message);
    }

    public UnknownJumpLabelException(String message, Throwable cause) {
        super(message, cause);
    }

}
