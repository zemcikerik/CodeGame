package dev.zemco.codegame.execution.io;

// TODO: explain satisfied

/**
 * Destination of output values during execution that expects values until satisfied.
 * @author Erik Zemčík
 */
public interface OutputSink {

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
