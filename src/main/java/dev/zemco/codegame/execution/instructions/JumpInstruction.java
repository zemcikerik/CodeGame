package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.IExecutionEngine;
import dev.zemco.codegame.execution.UnknownJumpLabelException;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;

public class JumpInstruction implements IInstruction {

    private final String label;

    public JumpInstruction(String label) {
        this.label = checkArgumentNotEmpty(label, "Label");
    }

    @Override
    public void execute(IExecutionContext executionContext) throws InstructionExecutionException {
        IExecutionEngine engine = executionContext.getExecutionEngine();

        try {
            engine.jumpToLabel(this.label);
        } catch (UnknownJumpLabelException e) {
            throw new InstructionExecutionException("Failed to perform a jump to label!", e);
        }
    }

}
