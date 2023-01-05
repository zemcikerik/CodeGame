package dev.zemco.codegame.compilation;

import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

/**
 * Thrown when a source code contains an invalid syntax.
 * @author Erik Zemčík
 */
public class InvalidSyntaxException extends Exception {

    private final int linePosition;

    /**
     * Creates an instance of {@link InvalidSyntaxException}.
     * @param message detail message containing information about the invalid syntax
     * @param linePosition zero-based line position where the syntax error is located
     * @throws IllegalArgumentException if {@code linePosition} is not a positive integer
     */
    public InvalidSyntaxException(String message, int linePosition) {
        super(message);
        this.linePosition = checkArgumentPositiveInteger(linePosition, "Line position");
    }

    /**
     * Creates an instance of {@link InvalidSyntaxException}.
     * @param message detail message containing information about the invalid syntax
     * @param cause cause of the invalid syntax error
     * @param linePosition zero-based line position where the syntax error is located
     * @throws IllegalArgumentException if {@code linePosition} is not a positive integer
     */
    public InvalidSyntaxException(String message, Throwable cause, int linePosition) {
        super(message, cause);
        this.linePosition = checkArgumentPositiveInteger(linePosition, "Line position");
    }

    /**
     * Returns the zero-based line position where the syntax error is located.
     * @return zero-based line position
     */
    public int getLinePosition() {
        return this.linePosition;
    }

}
