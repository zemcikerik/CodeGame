package dev.zemco.codegame.presentation;

public class ViewNotFoundException extends RuntimeException {

    public ViewNotFoundException() {
    }

    public ViewNotFoundException(String message) {
        super(message);
    }

    public ViewNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
