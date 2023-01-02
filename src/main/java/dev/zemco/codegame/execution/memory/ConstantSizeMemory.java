package dev.zemco.codegame.execution.memory;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Memory holding constant number of {@link IMemoryCell memory cells} specified during its creation.
 * @author Erik Zemčík
 */
public class ConstantSizeMemory implements IMemory {

    private static final int WORKING_CELL_INDEX = 0;
    private final IMemoryCell[] cells;

    /**
     * Creates an instance of {@link ConstantSizeMemory} that holds a specified number
     * of {@link IMemoryCell memory cells} created using the provided {@code memoryCellFactory}.
     *
     * @param size number of cells which memory should hold
     * @param memoryCellFactory factory to use to create new memory cells
     *
     * @throws IllegalArgumentException if {@code size} is not a positive non-zero integer or
     *                                  if {@code memoryCellFactory} is {@code null}
     */
    public ConstantSizeMemory(int size, IMemoryCellFactory memoryCellFactory) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be a positive non-zero integer!");
        }
        checkArgumentNotNull(memoryCellFactory, "Memory cell factory");

        this.cells = new IMemoryCell[size];

        for (int i = 0; i < size; i++) {
            this.cells[i] = memoryCellFactory.createMemoryCell();
        }
    }

    @Override
    public IMemoryCell getCellByAddress(int address) {
        if (address < 0 || address >= this.cells.length) {
            String message = String.format("Address %d was out of range <0, %d)!", address, this.cells.length);
            throw new IndexOutOfBoundsException(message);
        }
        return this.cells[address];
    }

    @Override
    public IMemoryCell getWorkingCell() {
        return this.cells[WORKING_CELL_INDEX];
    }

}
