package dev.zemco.codegame.util;

public final class Preconditions {

    public static <T> T checkArgumentNotNull(T argument, String argumentName) {
        if (argument == null) {
            String message = String.format("%s cannot be null!", argumentName != null ? argumentName : "Argument");
            throw new IllegalArgumentException(message);
        }
        return argument;
    }

    public static String checkArgumentNotEmpty(String argument, String argumentName) {
        if (argument != null && argument.isEmpty()) {
            String message = String.format("%s cannot be empty!", argumentName);
            throw new IllegalArgumentException(message);
        }
        return argument;
    }

    public static String checkArgumentNotNullAndNotEmpty(String argument, String argumentName) {
        checkArgumentNotNull(argument, argumentName);
        return checkArgumentNotEmpty(argument, argumentName);
    }

    private Preconditions() {
        // prevent instantiation of this class
    }

}
