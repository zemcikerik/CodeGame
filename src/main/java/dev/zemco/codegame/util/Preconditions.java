package dev.zemco.codegame.util;

import java.util.Collection;

public final class Preconditions {

    public static <T> T checkArgumentNotNull(T argument, String argumentName) {
        if (argument == null) {
            throwFormattedIllegalArgumentException("%s cannot be null!", argumentName);
        }
        return argument;
    }

    public static String checkArgumentNotEmpty(String argument, String argumentName) {
        if (argument != null && argument.isEmpty()) {
            throwFormattedIllegalArgumentException("%s cannot be empty!", argumentName);
        }
        return argument;
    }

    public static <T extends Collection<?>> T checkArgumentNotEmpty(T argument, String argumentName) {
        if (argument != null && argument.isEmpty()) {
            throwFormattedIllegalArgumentException("%s cannot be empty!", argumentName);
        }
        return argument;
    }

    public static String checkArgumentNotNullAndNotEmpty(String argument, String argumentName) {
        checkArgumentNotNull(argument, argumentName);
        return checkArgumentNotEmpty(argument, argumentName);
    }

    public static <T extends Collection<?>> T checkArgumentNotNullAndNotEmpty(T argument, String argumentName) {
        checkArgumentNotNull(argument, argumentName);
        return checkArgumentNotEmpty(argument, argumentName);
    }

    private static void throwFormattedIllegalArgumentException(String format, String argumentName) {
        String message = String.format(format, argumentName != null ? argumentName : "Argument");
        throw new IllegalArgumentException(message);
    }

    private Preconditions() {
        // prevent instantiation of this class
    }

}
