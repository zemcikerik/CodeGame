package dev.zemco.codegame.programs;

import java.util.List;
import java.util.Map;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Program is a set of instructions to be followed during execution.
 * Each program consists of {@link #getInstructionDescriptors() instruction descriptors} and
 * {@link #getJumpLabelToLinePositionMap() jump label mappings}.
 * <p>
 * Programs may be solutions to problems created by the player. Programs are immutable.
 *
 * @author Erik Zemčík
 */
public class Program {

    private final List<InstructionDescriptor> instructionDescriptors;
    private final Map<String, Integer> jumpLabelToLinePositionMap;

    /**
     * Creates an instance of {@link Program}.
     *
     * @param instructionDescriptors instruction descriptors {@link List list} of this program, ordered in a way
     *                               they should be executed
     * @param jumpLabelToLinePositionMap jump label to line position {@link Map map} of this program
     *
     * @throws IllegalArgumentException if {@code instructionDescriptors} is {@code null} or
     *                                  if {@code jumpLabelToLinePositionMap} is {@code null}
     */
    public Program(
        List<InstructionDescriptor> instructionDescriptors,
        Map<String, Integer> jumpLabelToLinePositionMap
    ) {
        checkArgumentNotNull(instructionDescriptors, "Instruction descriptors");
        checkArgumentNotNull(jumpLabelToLinePositionMap, "Jump label to line position map");

        this.instructionDescriptors = List.copyOf(instructionDescriptors);
        this.jumpLabelToLinePositionMap = Map.copyOf(jumpLabelToLinePositionMap);
    }

    /**
     * Returns {@link InstructionDescriptor instruction descriptors} of this program, ordered in a way they
     * should be executed.
     *
     * @return unmodifiable {@link List list} of {@link InstructionDescriptor instruction descriptors}
     */
    public List<InstructionDescriptor> getInstructionDescriptors() {
        return this.instructionDescriptors;
    }

    /**
     * Returns jump label to line position mappings used for jumping to a specific position during execution.
     * @return unmodifiable {@link Map map} of jump label mappings
     */
    public Map<String, Integer> getJumpLabelToLinePositionMap() {
        return this.jumpLabelToLinePositionMap;
    }

}
