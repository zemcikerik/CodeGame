package dev.zemco.codegame.execution.io;

/**
 * Produces {@link IInputSource input source} implementations.
 *
 * @author Erik Zemčík
 * @see IInputSource
 */
public interface IInputSourceFactory {

    /**
     * Creates a new {@link IInputSource input source} that provides its values from an {@link Iterable iterable}.
     *
     * @param iterable iterable to use as source of input values
     * @return input source
     */
    IInputSource createInputSourceFromIterable(Iterable<Integer> iterable);

}
