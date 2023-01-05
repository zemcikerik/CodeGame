package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Abstract base for instruction copying values from one {@link IMemoryCell memory cell} to some other.
 * @author Erik Zemčík
 */
public abstract class AbstractCopyInstruction implements IInstruction {

    /**
     * Retrieves the source {@link IMemoryCell memory cell} for copying.
     *
     * @param memory source of {@link IMemoryCell memory cells}
     * @return source {@link IMemoryCell memory cell}
     *
     * @throws InstructionExecutionException if the source {@link IMemoryCell memory cell} could not be retrieved
     */
    protected abstract IMemoryCell getSourceMemoryCell(IMemory memory) throws InstructionExecutionException;

    /**
     * Retrieves the destination {@link IMemoryCell memory cell} for copying.
     *
     * @param memory source of {@link IMemoryCell memory cells}
     * @return destination {@link IMemoryCell memory cell}
     *
     * @throws InstructionExecutionException if the destination {@link IMemoryCell memory cell} could not be retrieved
     */
    protected abstract IMemoryCell getDestinationMemoryCell(IMemory memory) throws InstructionExecutionException;

    /**
     * Copies the value from the source {@link IMemoryCell memory cell} to
     * the destination {@link IMemoryCell memory cell}.
     * <p>
     * Internally these {@link IMemoryCell memory cells} are resolved through the {@link #getSourceMemoryCell(IMemory)}
     * method and the {@link #getDestinationMemoryCell(IMemory)} respectively.
     *
     * @param executionContext context on which the instruction is executed
     * @throws IllegalArgumentException if {@code executionContext} is {@code null}
     * @throws InstructionExecutionException if the source {@link IMemoryCell memory cell} has no value
     */
    @Override
    public void execute(IExecutionContext executionContext) throws InstructionExecutionException {
        checkArgumentNotNull(executionContext, "Execution context");

        IMemory memory = executionContext.getMemory();
        IMemoryCell sourceCell = this.getSourceMemoryCell(memory);

        if (!sourceCell.hasValue()) {
            throw new InstructionExecutionException("Source cell holds no value!");
        }

        IMemoryCell destinationCell = this.getDestinationMemoryCell(memory);
        destinationCell.setValue(sourceCell.getValue());
    }

    /**
     * Retrieves a {@link IMemoryCell memory cell} at a given address.
     * This method is intended to be used by subclasses as it provides formatted exceptions
     * when the address is invalid for the given {@link IMemory memory}
     *
     * @param memory source of {@link IMemoryCell memory cells}
     * @param address address of the target {@link IMemoryCell memory cell}
     * @return {@link IMemoryCell memory cell} at the given address
     *
     * @throws InstructionExecutionException if the address is out of bounds of the {@link IMemory memory}
     */
    protected IMemoryCell getMemoryCellAtAddress(IMemory memory, int address) throws InstructionExecutionException {
        try {
            return memory.getCellByAddress(address);
        } catch (IndexOutOfBoundsException e) {
            String message = String.format("Memory does not hold cell at address %d!", address);
            throw new InstructionExecutionException(message, e);
        }
    }

}
