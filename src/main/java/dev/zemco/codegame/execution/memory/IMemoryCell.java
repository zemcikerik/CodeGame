package dev.zemco.codegame.execution.memory;

/**
 * Cell in a memory that can hold a single integer value.
 * Cells may hold no value at any point during their lifetime.
 * @author Erik Zemčík
 */
public interface IMemoryCell {

    /**
     * Returns if cell currently holds an integer value.
     * @return true if currently holds value else false
     */
    boolean hasValue();

    /**
     * Returns currently held integer value of this cell.
     * @return currently held value
     * @throws IllegalStateException if cell does not hold a value
     */
    int getValue();

    /**
     * Sets value held by the cell.
     * @param value value to set
     */
    void setValue(int value);

}
