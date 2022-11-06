package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.ExecutionContext;
import dev.zemco.codegame.execution.io.InputSource;
import dev.zemco.codegame.execution.memory.Memory;
import dev.zemco.codegame.execution.memory.MemoryCell;

/**
 * Instruction that takes next value from input source and copies it to working memory cell.
 * @author Erik Zemčík
 */
public class InputInstruction implements Instruction {

    /**
     * Takes next value from input source and copies it to working memory cell.
     * @param executionContext context on which the instruction is executed
     * @throws InstructionExecutionException if input source has no next value
     */
    @Override
    public void execute(ExecutionContext executionContext) throws InstructionExecutionException {
        InputSource inputSource = executionContext.getInputSource();

        if (!inputSource.hasNextValue()) {
            throw new InstructionExecutionException("Input source has no next value!");
        }

        Memory memory = executionContext.getMemory();
        MemoryCell workingCell = memory.getWorkingCell();
        workingCell.setValue(inputSource.getNextValue());
    }

}
