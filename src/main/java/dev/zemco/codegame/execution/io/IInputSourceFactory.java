package dev.zemco.codegame.execution.io;

/**
 * Produces {@link IInputSource input sources}.
 *
 * @author Erik Zemčík
 * @see IInputSource
 */
public interface IInputSourceFactory {

    /**
     * Creates an {@link IInputSource input source} that provides its values from an {@link Iterable iterable}.
     *
     * @param iterable iterable to use as source of input values
     * @return input source
     * @throws IllegalArgumentException if {@code iterable} is {@code null}
     */
    IInputSource createInputSourceFromIterable(Iterable<Integer> iterable);

}
