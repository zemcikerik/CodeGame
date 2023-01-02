package dev.zemco.codegame.execution.io;

/**
 * Produces {@link IOutputSink output sinks}.
 *
 * @author Erik Zemčík
 * @see IOutputSink
 */
public interface IOutputSinkFactory {

    /**
     * Creates an {@link IOutputSink output sink} that verifies output values
     * by matching them to values from an {@link Iterable iterable}.
     *
     * @param iterable iterable to match values against
     * @return verifying output sink
     * @throws IllegalArgumentException if {@code iterable} is {@code null}
     */
    IOutputSink createVerifyingOutputSinkFromIterable(Iterable<Integer> iterable);

}
