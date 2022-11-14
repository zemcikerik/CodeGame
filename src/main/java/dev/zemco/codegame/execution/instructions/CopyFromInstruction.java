package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

public class CopyFromInstruction extends AbstractCopyInstruction {

    private final int sourceCellAddress;

    public CopyFromInstruction(int sourceCellAddress) {
        // TODO: check validity of address
        this.sourceCellAddress = sourceCellAddress;
    }

    @Override
    protected IMemoryCell getSourceMemoryCell(IMemory memory) throws InstructionExecutionException {
        return this.getMemoryCellAtAddress(memory, this.sourceCellAddress);
    }

    @Override
    protected IMemoryCell getDestinationMemoryCell(IMemory memory) {
        return memory.getWorkingCell();
    }

}
