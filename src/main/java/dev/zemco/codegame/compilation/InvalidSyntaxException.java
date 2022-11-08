package dev.zemco.codegame.compilation;

public class InvalidSyntaxException extends Exception {

    private final int linePosition;

    public InvalidSyntaxException(String message, int linePosition) {
        super(message);
        this.linePosition = linePosition;
    }

    public InvalidSyntaxException(String message, Throwable cause, int linePosition) {
        super(message, cause);
        this.linePosition = linePosition;
    }

    public int getLinePosition() {
        return this.linePosition;
    }

}
