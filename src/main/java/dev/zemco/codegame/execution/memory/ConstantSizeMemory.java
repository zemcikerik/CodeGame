package dev.zemco.codegame.execution.memory;

public class ConstantSizeMemory implements IMemory {

    private static final int WORKING_CELL_INDEX = 0;
    private final IMemoryCell[] cells;

    public ConstantSizeMemory(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be a positive non-zero integer!");
        }

        this.cells = new IMemoryCell[size];

        for (int i = 0; i < size; i++) {
            this.cells[i] = new SimpleMemoryCell();
        }
    }

    @Override
    public IMemoryCell getCellByAddress(int address) {
        if (address < 0) {
            throw new IllegalArgumentException("Address must be a positive integer!");
        }
        if (address >= this.cells.length) {
            throw new IndexOutOfBoundsException("Address references cell out of range!");
        }
        return this.cells[address];
    }

    @Override
    public IMemoryCell getWorkingCell() {
        return this.cells[WORKING_CELL_INDEX];
    }

}
