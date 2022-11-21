package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

public class CopyFromInstruction extends AbstractCopyInstruction {

    private final int sourceCellAddress;

    public CopyFromInstruction(int sourceCellAddress) {
        this.sourceCellAddress = checkArgumentPositiveInteger(sourceCellAddress, "Source cell address");
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
