package dev.zemco.codegame.util;

import java.util.Collection;

/**
 * Collection of utility functions for checking validity of arguments.
 * @author Erik Zemčík
 */
public final class Preconditions {

    /**
     * Throws formatted {@link IllegalArgumentException} if {@code argument} is {@code null}.
     *
     * @param <T> argument type
     * @param argument argument to check
     * @param argumentName name of the checked argument
     * @return non-null instance passed to {@code argument}
     *
     * @throws IllegalArgumentException if {@code argument} is {@code null}
     */
    public static <T> T checkArgumentNotNull(T argument, String argumentName) {
        if (argument == null) {
            throwFormattedIllegalArgumentException("%s cannot be null!", argumentName);
        }

        return argument;
    }

    /**
     * Throws formatted {@link IllegalArgumentException} if {@link String} {@code argument}
     * is {@code null} or has no characters.
     *
     * @param argument argument to check
     * @param argumentName name of the checked argument
     * @return instance passed to {@code argument}
     *
     * @throws IllegalArgumentException if {@code argument} is {@code null} or empty
     */
    public static String checkArgumentNotEmpty(String argument, String argumentName) {
        checkArgumentNotNull(argument, argumentName);

        if (argument.isEmpty()) {
            throwFormattedIllegalArgumentException("%s cannot be empty!", argumentName);
        }

        return argument;
    }

    /**
     * Throws formatted {@link IllegalArgumentException} if {@link Collection} {@code argument}
     * is {@code null} or contains no elements.
     *
     * @param <T> collection argument type
     * @param argument argument to check
     * @param argumentName name of the checked argument
     * @return instance passed to {@code argument}
     *
     * @throws IllegalArgumentException if {@code argument} is {@code null} or empty
     */
    public static <T extends Collection<?>> T checkArgumentNotEmpty(T argument, String argumentName) {
        checkArgumentNotNull(argument, argumentName);

        if (argument.isEmpty()) {
            throwFormattedIllegalArgumentException("%s cannot be empty!", argumentName);
        }

        return argument;
    }

    /**
     * Throws formatted {@link IllegalArgumentException} if {@code argument} is less than {@code 0}.
     *
     * @param argument argument to check
     * @param argumentName name of the checked argument
     * @return {@code int} passed to {@code argument}
     *
     * @throws IllegalArgumentException if {@code argument} is less than {@code 0}
     */
    public static int checkArgumentPositiveInteger(int argument, String argumentName) {
        if (argument < 0) {
            throwFormattedIllegalArgumentException("%s must be a positive integer!", argumentName);
        }

        return argument;
    }

    private static void throwFormattedIllegalArgumentException(String format, String argumentName) {
        String message = String.format(format, argumentName != null ? argumentName : "Argument");
        throw new IllegalArgumentException(message);
    }

    private Preconditions() {
        // prevent instantiation of this class
    }

}
