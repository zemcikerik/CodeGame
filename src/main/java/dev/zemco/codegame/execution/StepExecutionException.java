package dev.zemco.codegame.execution;

import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

public class StepExecutionException extends RuntimeException {

    private final int linePosition;

    public StepExecutionException(String message, Throwable cause, int linePosition) {
        super(message, cause);
        this.linePosition = checkArgumentPositiveInteger(linePosition, "Line position");
    }

    public int getLinePosition() {
        return this.linePosition;
    }
    
}
