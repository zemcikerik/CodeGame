package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.ExecutionContext;
import dev.zemco.codegame.execution.io.NotAcceptedException;
import dev.zemco.codegame.execution.io.OutputSink;
import dev.zemco.codegame.execution.memory.Memory;
import dev.zemco.codegame.execution.memory.MemoryCell;

/**
 * Instruction that emits current value in working cell to output sink.
 * @author Erik Zemčík
 */
public class OutputInstruction implements Instruction {

    /**
     * Emits current value in working cell to output sink.
     * @param executionContext context on which the instruction is executed
     * @throws InstructionExecutionException if working cell has no value or output sink rejects the value
     */
    @Override
    public void execute(ExecutionContext executionContext) throws InstructionExecutionException {
        Memory memory = executionContext.getMemory();
        MemoryCell workingCell = memory.getWorkingCell();

        if (!workingCell.hasValue()) {
            throw new InstructionExecutionException("Working cell does not hold value!");
        }

        OutputSink outputSink = executionContext.getOutputSink();

        try {
            outputSink.accept(workingCell.getValue());
        } catch (NotAcceptedException e) {
            throw new InstructionExecutionException("Output sink rejected the value from working cell!");
        }
    }

}
