package dev.zemco.codegame.programs;

import dev.zemco.codegame.execution.instructions.IInstruction;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

/**
 * Instruction descriptors provide additional context to individual {@link IInstruction instructions} during
 * the program execution.
 * <p>
 * Instruction descriptors are immutable.
 *
 * @author Erik Zemčík
 */
public class InstructionDescriptor {

    private final IInstruction instruction;
    private final int linePosition;

    /**
     * Creates an instance of {@link InstructionDescriptor} for a specific {@link IInstruction instruction}.
     *
     * @param instruction instruction to provide context for
     * @param linePosition zero-based line position associated with the instruction
     *
     * @throws IllegalArgumentException if {@code instruction} is {@code null} or
     *                                  if {@code linePosition} is not a positive integer
     */
    public InstructionDescriptor(IInstruction instruction, int linePosition) {
        this.instruction = checkArgumentNotNull(instruction, "Instruction");
        this.linePosition = checkArgumentPositiveInteger(linePosition, "Line position");
    }

    /**
     * Returns the {@link IInstruction instruction} this descriptor provides the additional context for.
     * @return instruction
     */
    public IInstruction getInstruction() {
        return this.instruction;
    }

    /**
     * Returns the zero-based line position associated with the {@link IInstruction instruction}.
     * @return zero-based line position
     */
    public int getLinePosition() {
        return this.linePosition;
    }

}
