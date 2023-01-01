package dev.zemco.codegame.execution;

public class StepExecutionException extends RuntimeException {

    private final int linePosition;

    public StepExecutionException(String message, Throwable cause, int linePosition) {
        super(message, cause);
        this.linePosition = linePosition;
    }

    public int getLinePosition() {
        return linePosition;
    }
    
}
