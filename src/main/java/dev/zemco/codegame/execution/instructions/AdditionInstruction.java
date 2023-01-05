package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Instruction that adds constant to value held in working {@link IMemoryCell memory cell}.
 * @author Erik Zemčík
 */
public class AdditionInstruction implements IInstruction {

    private final int addend;

    /**
     * Creates an instance of {@link AdditionInstruction} with the specified addition constant.
     * @param addend constant to add to working {@link IMemoryCell memory cell} during execution
     */
    public AdditionInstruction(int addend) {
        this.addend = addend;
    }

    /**
     * Adds constant to value held in working {@link IMemoryCell memory cell}.
     * @param executionContext context on which the instruction is executed
     *
     * @throws IllegalArgumentException if {@code executionContext} is {@code null}
     * @throws InstructionExecutionException if working {@link IMemoryCell memory cell} has no value
     */
    @Override
    public void execute(IExecutionContext executionContext) throws InstructionExecutionException {
        checkArgumentNotNull(executionContext, "Execution context");

        IMemory memory = executionContext.getMemory();
        IMemoryCell workingCell = memory.getWorkingCell();

        if (!workingCell.hasValue()) {
            throw new InstructionExecutionException("Working cell does not hold value!");
        }

        workingCell.setValue(workingCell.getValue() + this.addend);
    }

}
