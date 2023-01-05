package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

/**
 * Instruction that copies value from working {@link IMemoryCell memory cell} to target {@link IMemoryCell memory cell}.
 * @author Erik Zemčík
 */
public class CopyToInstruction extends AbstractCopyInstruction {

    private final int destinationCellAddress;

    /**
     * Creates an instance of {@link CopyToInstruction} that copies values into {@link IMemoryCell memory cell}
     * at a given address.
     *
     * @param destinationCellAddress address of the destination {@link IMemoryCell memory cell}
     * @throws IllegalArgumentException if {@code destinationCellAddress} is not a positive integer
     */
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
