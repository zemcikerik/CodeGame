package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;

/**
 * Instruction is an elementary operation that modifies state of the execution context.
 * Each instruction execution lasts a single step.
 * @author Erik Zemčík
 */
public interface IInstruction {

    /**
     * Executes instruction on a given context.
     * @param executionContext context on which the instruction is executed
     * @throws InstructionExecutionException if illegal state during execution is reached
     */
    void execute(IExecutionContext executionContext) throws InstructionExecutionException;

}
