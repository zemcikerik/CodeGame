package dev.zemco.codegame.programs;

import dev.zemco.codegame.execution.instructions.IInstruction;

/**
 * Interface for simplifying {@link Program program} creation process.
 * This interface is useful when full {@link Program program} context has not been fully created,
 * but will be at later point. The final {@link Program program} can be built using the {@link #build()} method.
 *
 * @author Erik Zemčík
 * @see Program
 */
public interface IProgramBuilder {

    /**
     * Appends {@link IInstruction instruction} to all instructions of the {@link Program program}.
     *
     * @param instruction {@link IInstruction instruction} to add
     * @param linePosition line position associated with the {@link IInstruction instruction}
     *
     * @throws IllegalArgumentException if {@code instruction} is {@code null} or
     *                                  if {@code linePosition} is not a positive integer
     */
    void addInstruction(IInstruction instruction, int linePosition);

    /**
     * Adds unique jump label mapping to the {@link Program program}.
     * If addition of the same jump label is attempted, this method should throw {@link IllegalStateException}.
     * Dedicated method {@link #hasJumpLabelMapping(String)} can be used to check if label has been already added.
     *
     * @param label jump label of the mapping
     * @param linePosition zero-based line position to jump to
     *
     * @throws IllegalArgumentException if {@code label} is {@code null} or empty or
     *                                  if {@code linePosition} is not a positive integer
     * @throws IllegalStateException if {@code label} is already assigned to different mapping
     *
     * @see Program#getJumpLabelToLinePositionMap()
     */
    void addJumpLabelMapping(String label, int linePosition);

    /**
     * Check if jump label has been added to the {@link Program program}.
     *
     * @param label jump label to check
     * @return true if the builder already contains the jump label, else false
     *
     * @throws IllegalArgumentException if {@code label} is {@code null} or empty
     */
    boolean hasJumpLabelMapping(String label);

    /**
     * Builds the {@link Program program} based on the data specified during the lifetime of the builder.
     * @return program
     */
    Program build();

}
