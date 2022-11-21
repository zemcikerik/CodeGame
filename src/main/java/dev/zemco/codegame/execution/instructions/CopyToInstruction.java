package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

public class CopyToInstruction extends AbstractCopyInstruction {

    private final int destinationCellAddress;

    public CopyToInstruction(int destinationCellAddress) {
        this.destinationCellAddress = checkArgumentPositiveInteger(destinationCellAddress, "Destination cell address");
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
