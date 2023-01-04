package dev.zemco.codegame.presentation;

/**
 * Thrown when navigation is attempted, but the route is not known by the navigator.
 * @author Erik Zemčík
 */
public class UnknownRouteException extends RuntimeException {

    /**
     * Creates an instance of {@link UnknownRouteException}.
     * @param message detail message containing information about the navigation failure
     * @param cause cause of the resource navigation failure
     */
    public UnknownRouteException(String message, Throwable cause) {
        super(message, cause);
    }

}
