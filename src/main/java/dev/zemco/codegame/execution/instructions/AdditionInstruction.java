package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

// TODO: abstract away binary operation

/**
 * {@link IInstruction Instruction} that adds constant to value held in working cell.
 * @author Erik Zemčík
 */
public class AdditionInstruction implements IInstruction {

    private final int addend;

    /**
     * Creates an instance of {@link AdditionInstruction} with addition constant.
     * @param addend constant to add to working cell during execution
     */
    public AdditionInstruction(int addend) {
        this.addend = addend;
    }

    /**
     * Adds constant to value held in working cell.
     * @param executionContext context on which the instruction is executed
     * @throws InstructionExecutionException if working cell has no value
     */
    @Override
    public void execute(IExecutionContext executionContext) throws InstructionExecutionException {
        IMemory memory = executionContext.getMemory();
        IMemoryCell workingCell = memory.getWorkingCell();

        if (!workingCell.hasValue()) {
            throw new InstructionExecutionException("Working cell does not hold value!");
        }

        workingCell.setValue(workingCell.getValue() + this.addend);
    }

}
