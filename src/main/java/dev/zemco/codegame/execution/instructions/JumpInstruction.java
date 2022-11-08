package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.ExecutionContext;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNullAndNotEmpty;

public class JumpInstruction implements Instruction {

    private final String label;

    public JumpInstruction(String label) {
        this.label = checkArgumentNotNullAndNotEmpty(label, "Label");
    }

    @Override
    public void execute(ExecutionContext executionContext) throws InstructionExecutionException {
        executionContext.getExecutionEngine().jumpTo(this.label);
    }

}
