package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.io.IInputSource;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Instruction that takes next value from {@link IInputSource input source} and copies it to the working
 * {@link IMemoryCell memory cell}.
 *
 * @author Erik Zemčík
 */
public class InputInstruction implements IInstruction {

    /**
     * Takes next value from {@link IInputSource input source} and copies it to
     * the working {@link IMemoryCell memory cell}.
     *
     * @param executionContext context on which the instruction is executed
     * @throws IllegalArgumentException if {@code executionContext} is {@code null}
     * @throws InstructionExecutionException if input source has no next value
     */
    @Override
    public void execute(IExecutionContext executionContext) throws InstructionExecutionException {
        checkArgumentNotNull(executionContext, "Execution context");
        IInputSource inputSource = executionContext.getInputSource();

        if (!inputSource.hasNextValue()) {
            throw new InstructionExecutionException("Input source has no next value!");
        }

        IMemory memory = executionContext.getMemory();
        IMemoryCell workingCell = memory.getWorkingCell();
        workingCell.setValue(inputSource.getNextValue());
    }

}
