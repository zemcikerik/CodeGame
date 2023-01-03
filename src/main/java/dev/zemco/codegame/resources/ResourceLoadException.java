package dev.zemco.codegame.resources;

/**
 * Thrown when an error occurs during application resource loading.
 * @author Erik Zemčík
 */
public class ResourceLoadException extends RuntimeException {

    /**
     * Creates an instance of {@link ResourceLoadException}.
     * @param message detail message containing information about the load failure
     * @param cause cause of the resource load failure
     */
    public ResourceLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}
