package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

public class CopyToInstruction extends AbstractCopyInstruction {

    private final int destinationCellAddress;

    public CopyToInstruction(int destinationCellAddress) {
        // TODO: check address validity
        this.destinationCellAddress = destinationCellAddress;
    }

    @Override
    protected IMemoryCell getSourceMemoryCell(IMemory memory) {
        return memory.getWorkingCell();
    }

    @Override
    protected IMemoryCell getDestinationMemoryCell(IMemory memory) throws InstructionExecutionException {
        return this.getMemoryCellAtAddress(memory, this.destinationCellAddress);
    }

}
