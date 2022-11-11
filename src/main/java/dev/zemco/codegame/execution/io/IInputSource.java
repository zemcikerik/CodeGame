package dev.zemco.codegame.execution.io;

/**
 * Source of input values during execution.
 * @author Erik Zemčík
 */
public interface IInputSource {

    /**
     * Returns true if input source has more values.
     * @return true if input source has more values else false
     */
    boolean hasNextValue();

    /**
     * Returns next value of the input source.
     * @return next value of the input source
     * @throws java.util.NoSuchElementException if it has no next value
     */
    int getNextValue();

}
