package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.engine.IExecutionEngine;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

/**
 * Instruction that performs a jump of the {@link IExecutionEngine engine} to the specified label if
 * the value of the working {@link IMemoryCell memory cell} is equal to zero.
 *
 * @author Erik Zemčík
 */
public class JumpIfZeroInstruction extends JumpInstruction {

    /**
     * Creates an instance of {@link JumpIfZeroInstruction} with the target jump label.
     * @param label target label of the jump
     * @throws IllegalArgumentException if {@code label} is {@code null} or empty
     */
    public JumpIfZeroInstruction(String label) {
        super(label);
    }

    /**
     * Checks if the value of the working {@link IMemoryCell memory cell} is equal to zero.
     *
     * @param executionContext context on which the instruction is executed
     * @return true if the value of the working {@link IMemoryCell memory cell} is equal to zero, else false
     *
     * @throws InstructionExecutionException if the working {@link IMemoryCell memory cell} holds no value
     */
    @Override
    protected boolean shouldPerformJump(IExecutionContext executionContext) throws InstructionExecutionException {
        IMemory memory = executionContext.getMemory();
        IMemoryCell workingCell = memory.getWorkingCell();

        if (!workingCell.hasValue()) {
            throw new InstructionExecutionException("Working cell does not hold value!");
        }

        return workingCell.getValue() == 0;
    }

}
