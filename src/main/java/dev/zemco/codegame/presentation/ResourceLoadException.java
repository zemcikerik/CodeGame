package dev.zemco.codegame.presentation;

public class ResourceLoadException extends RuntimeException {

    public ResourceLoadException() {
    }

    public ResourceLoadException(String message) {
        super(message);
    }

    public ResourceLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}
