package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

public class JumpIfZeroInstruction extends JumpInstruction {

    public JumpIfZeroInstruction(String label) {
        super(label);
    }

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
