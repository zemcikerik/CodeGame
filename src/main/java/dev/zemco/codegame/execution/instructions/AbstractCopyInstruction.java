package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

public abstract class AbstractCopyInstruction implements IInstruction {

    protected abstract IMemoryCell getSourceMemoryCell(IMemory memory) throws InstructionExecutionException;
    protected abstract IMemoryCell getDestinationMemoryCell(IMemory memory) throws InstructionExecutionException;

    @Override
    public void execute(IExecutionContext executionContext) throws InstructionExecutionException {
        IMemory memory = executionContext.getMemory();
        IMemoryCell sourceCell = this.getSourceMemoryCell(memory);

        if (!sourceCell.hasValue()) {
            throw new InstructionExecutionException("Source cell holds no value!");
        }

        IMemoryCell destinationCell = this.getDestinationMemoryCell(memory);
        destinationCell.setValue(sourceCell.getValue());
    }

    protected IMemoryCell getMemoryCellAtAddress(IMemory memory, int address) throws InstructionExecutionException {
        try {
            return memory.getCellByAddress(address);
        } catch (IndexOutOfBoundsException e) {
            String message = String.format("Memory does not hold cell at address %d!", address);
            throw new InstructionExecutionException(message, e);
        }
    }

}
