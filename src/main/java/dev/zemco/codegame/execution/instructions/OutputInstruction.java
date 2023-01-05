package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.io.NotAcceptedException;
import dev.zemco.codegame.execution.io.IOutputSink;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Instruction that emits current value in working {@link IMemoryCell memory cell} to {@link IOutputSink output sink}.
 * @author Erik Zemčík
 */
public class OutputInstruction implements IInstruction {

    /**
     * Emits current value in working {@link IMemoryCell memory cell} to {@link IOutputSink output sink}.
     * @param executionContext context on which the instruction is executed
     *
     * @throws IllegalArgumentException if {@code executionContext} is {@code null}
     * @throws InstructionExecutionException if working {@link IMemoryCell memory cell} has no value or
     *                                       {@link IOutputSink output sink} rejects the value
     */
    @Override
    public void execute(IExecutionContext executionContext) throws InstructionExecutionException {
        checkArgumentNotNull(executionContext, "Execution context");

        IMemory memory = executionContext.getMemory();
        IMemoryCell workingCell = memory.getWorkingCell();

        if (!workingCell.hasValue()) {
            throw new InstructionExecutionException("Working cell does not hold value!");
        }

        IOutputSink outputSink = executionContext.getOutputSink();

        try {
            outputSink.accept(workingCell.getValue());
        } catch (NotAcceptedException e) {
            throw new InstructionExecutionException("Output sink rejected the value from working cell!", e);
        }
    }

}
