package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.engine.IExecutionEngine;
import dev.zemco.codegame.execution.engine.UnknownJumpLabelException;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Instruction that performs a jump of the {@link IExecutionEngine engine} to the specified label.
 * @author Erik Zemčík
 */
public class JumpInstruction implements IInstruction {

    private final String label;

    /**
     * Creates an instance of {@link JumpInstruction} with the target jump label.
     * @param label target label of the jump
     * @throws IllegalArgumentException if {@code label} is {@code null} or empty
     */
    public JumpInstruction(String label) {
        this.label = checkArgumentNotEmpty(label, "Label");
    }

    /**
     * Requests the {@link IExecutionEngine engine} to perform a jump to the target jump label.
     * @param executionContext context on which the instruction is executed
     * @throws IllegalArgumentException if {@code executionContext} is {@code null}
     * @throws InstructionExecutionException if the target label is not known by the {@link IExecutionEngine engine}
     */
    @Override
    public void execute(IExecutionContext executionContext) throws InstructionExecutionException {
        checkArgumentNotNull(executionContext, "Execution context");
        IExecutionEngine engine = executionContext.getExecutionEngine();

        if (!this.shouldPerformJump(executionContext)) {
            return;
        }

        try {
            engine.jumpToLabel(this.label);
        } catch (UnknownJumpLabelException e) {
            throw new InstructionExecutionException("Failed to perform a jump to label!", e);
        }
    }

    /**
     * Checks if the instruction should request the {@link IExecutionEngine engine} to perform a jump.
     * This method is intended to be overridden by subclasses requiring more advanced jumping logic.
     *
     * @param executionContext context on which the instruction is executed
     * @return true if the jump should be performed, else false
     *
     * @throws InstructionExecutionException if illegal state during the check is reached
     */
    protected boolean shouldPerformJump(IExecutionContext executionContext) throws InstructionExecutionException {
        return true;
    }

}
