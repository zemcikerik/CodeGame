package dev.zemco.codegame.execution;

public class StepExecutionException extends RuntimeException {

    public StepExecutionException() {
        super();
    }

    public StepExecutionException(String message) {
        super(message);
    }

    public StepExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
