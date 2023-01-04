package dev.zemco.codegame.presentation;

/**
 * Thrown when a component does not recognize a view.
 * This exception is typically seen when an invalid view id was used.
 *
 * @author Erik Zemčík
 */
public class UnknownViewException extends RuntimeException {

    /**
     * Creates an instance of {@link UnknownViewException}.
     * @param message detail message containing information about the view recognition failure
     */
    public UnknownViewException(String message) {
        super(message);
    }

}
