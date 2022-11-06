package dev.zemco.codegame.execution.io;

// TODO: javadoc
public class NotAcceptedException extends RuntimeException {

    public NotAcceptedException() {
    }

    public NotAcceptedException(String message) {
        super(message);
    }

    public NotAcceptedException(String message, Throwable cause) {
        super(message, cause);
    }

}
