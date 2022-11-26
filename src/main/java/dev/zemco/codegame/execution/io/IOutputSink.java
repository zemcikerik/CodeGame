package dev.zemco.codegame.execution.io;

/**
 * Destination of output values during execution that expects values until
 * it becomes {@link #isSatisfied() satisfied}.
 *
 * @author Erik Zemčík
 * @see IInputSource
 */
public interface IOutputSink {

    /**
     * Attempts to write {@code value} to output.
     * @param value value to output
     * @throws NotAcceptedException if {@code value} is not accepted output value
     */
    void accept(int value);

    /**
     * Returns if sink expects another value.
     * @return true if sink expects another value else false
     */
    boolean isSatisfied();

}
