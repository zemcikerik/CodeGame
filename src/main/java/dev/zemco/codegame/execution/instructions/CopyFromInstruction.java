package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

/**
 * Instruction that copies value from target {@link IMemoryCell memory cell} to working {@link IMemoryCell memory cell}.
 * @author Erik Zemčík
 */
public class CopyFromInstruction extends AbstractCopyInstruction {

    private final int sourceCellAddress;

    /**
     * Creates an instance of {@link CopyFromInstruction} that copies values from {@link IMemoryCell memory cell}
     * at a given address.
     *
     * @param sourceCellAddress address of the source {@link IMemoryCell memory cell}
     * @throws IllegalArgumentException if {@code sourceCellAddress} is not a positive integer
     */
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
