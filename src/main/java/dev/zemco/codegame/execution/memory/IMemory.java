package dev.zemco.codegame.execution.memory;

/**
 * Collection of {@link IMemoryCell memory cells} which can hold an integer value.
 * Each cell can be accessed using its address.
 * <p>
 * Memory also contains {@link #getWorkingCell() working cell}, that has a special position
 * during execution as it is used as target variable for some instructions. Working cell can be accessed
 * either through dedicated method {@link #getWorkingCell()} or its constant address {@code 0}.
 *
 * @author Erik Zemčík
 * @see IMemoryCell
 */
public interface IMemory {

    /**
     * Retrieves a {@link IMemoryCell cell} at the specified address.
     *
     * @param address address of the cell to return
     * @return cell at the specified address
     * @throws IndexOutOfBoundsException if address is out of range of the memory
     */
    IMemoryCell getCellByAddress(int address);

    /**
     * Retrieves the working {@link IMemoryCell cell}.
     * @return working cell
     */
    IMemoryCell getWorkingCell();

}
