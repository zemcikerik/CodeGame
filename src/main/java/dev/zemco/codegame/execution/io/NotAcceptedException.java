package dev.zemco.codegame.execution.io;

/**
 * Thrown by {@link IOutputSink output sink} after it rejects an output value.
 *
 * @author Erik Zemčík
 * @see IOutputSink
 */
public class NotAcceptedException extends RuntimeException {

    /**
     * Creates an instance of {@link NotAcceptedException}.
     * @param message detail message containing information about the rejection
     */
    public NotAcceptedException(String message) {
        super(message);
    }

}
