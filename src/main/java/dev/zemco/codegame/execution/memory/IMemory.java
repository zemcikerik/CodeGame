package dev.zemco.codegame.execution.memory;

/**
 * Collection of memory cells which can hold an integer value.
 * Each cell can be accessed using its address.
 * <p>
 * Memory also contains working cell, that has a special position
 * during execution as it is used as target variable for some instructions.
 * <p>
 * Working cell can be accessed either through dedicated method
 * {@link #getWorkingCell()} or its constant address {@code 0}.
 *
 * @author Erik Zemčík
 * @see IMemoryCell
 */
public interface IMemory {

    /**
     * Retrieves cell at a specified address.
     * Can be also used to access working cell by using address {@code 0}.
     * TODO: is this fine?
     *
     * @param address address of cell to return
     * @return cell at the specified address
     * @throws IndexOutOfBoundsException if address is out of range of memory
     */
    IMemoryCell getCellByAddress(int address);

    /**
     * Retrieves working cell.
     * @return working cell
     */
    IMemoryCell getWorkingCell();

}
