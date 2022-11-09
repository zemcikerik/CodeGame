package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.ExecutionContext;
import dev.zemco.codegame.execution.ExecutionEngine;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNullAndNotEmpty;

public class JumpInstruction implements Instruction {

    private final String label;

    public JumpInstruction(String label) {
        this.label = checkArgumentNotNullAndNotEmpty(label, "Label");
    }

    @Override
    public void execute(ExecutionContext executionContext) throws InstructionExecutionException {
        ExecutionEngine engine = executionContext.getExecutionEngine();

        try {
            engine.jumpTo(this.label);
            // TODO: catch proper exception once exception type is decided
        } catch (RuntimeException e) {
            throw new InstructionExecutionException("Failed to perform a jump to label!", e);
        }
    }

}
