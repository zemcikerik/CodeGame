package dev.zemco.codegame.execution.io;

/**
 * Produces {@link IOutputSink output sink} implementations.
 *
 * @author Erik Zemčík
 * @see IOutputSink
 */
public interface IOutputSinkFactory {

    /**
     * Creates a new {@link IOutputSink output sink} that verifies output values
     * by matching them to values from an {@link Iterable iterable}.
     *
     * @param iterable iterable to match values against
     * @return verifying output sink
     */
    IOutputSink createVerifyingOutputSinkFromIterable(Iterable<Integer> iterable);

}
