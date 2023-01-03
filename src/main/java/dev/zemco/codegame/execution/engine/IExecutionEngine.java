package dev.zemco.codegame.execution.engine;

import dev.zemco.codegame.compilation.IInstructionDescriptor;
import dev.zemco.codegame.execution.IExecutionContext;

import java.util.Optional;

/**
 * Engine responsible for an instruction execution within its {@link IExecutionContext execution context}.
 * If any method provided by this interface throws an exception, entire {@link IExecutionContext execution context}
 * should be considered invalid unless explicitly specified by the implementation.
 *
 * @author Erik Zemčík
 */
public interface IExecutionEngine {

    /**
     * Performs a jump to an instruction that is followed by the specified {@code label}.
     * This label must be known by the engine or an exception is thrown.
     *
     * @param label label to jump to
     * @throws IllegalArgumentException if {@code label} is {@code null} or empty
     * @throws UnknownJumpLabelException if {@code label} is not known by the engine
     */
    void jumpToLabel(String label);

    /**
     * Returns a descriptor of the instruction that will be executed next by the engine.
     * <p>
     * This method returns an empty {@link Optional} if the engine has no next instruction
     * or if the retrieval is not supported by the implementation.
     *
     * @return instruction descriptor of the next instruction or an empty {@link Optional}
     */
    Optional<IInstructionDescriptor> getNextInstructionDescriptor();

    /**
     * Executes a single instruction on the engine's {@link IExecutionContext execution context} and moves to the next.
     * @throws NoNextInstructionException if engine has no next instruction available
     * @throws StepExecutionException if execution of the instruction fails
     */
    void step();

}
