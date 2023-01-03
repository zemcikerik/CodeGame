package dev.zemco.codegame.programs;

import dev.zemco.codegame.execution.instructions.IInstruction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

/**
 * Simple implementation for building {@link Program programs} based on the required rules.
 * @author Erik Zemčík
 */
public class ProgramBuilder implements IProgramBuilder {

    private final List<InstructionDescriptor> instructionDescriptors;
    private final Map<String, Integer> jumpLabelToLinePositionMap;

    /**
     * Creates an instance of {@link ProgramBuilder} ready for use.
     */
    public ProgramBuilder() {
        this.instructionDescriptors = new LinkedList<>();
        this.jumpLabelToLinePositionMap = new HashMap<>();
    }

    @Override
    public void addInstruction(IInstruction instruction, int linePosition) {
        checkArgumentNotNull(instruction, "Instruction");
        checkArgumentPositiveInteger(linePosition, "Line position");

        this.instructionDescriptors.add(new InstructionDescriptor(instruction, linePosition));
    }

    @Override
    public void addJumpLabelMapping(String label, int linePosition) {
        checkArgumentNotEmpty(label, "Label");
        checkArgumentPositiveInteger(linePosition, "Line position");

        if (this.jumpLabelToLinePositionMap.containsKey(label)) {
            String message = String.format("Label '%s' is already defined!", label);
            throw new IllegalStateException(message);
        }

        this.jumpLabelToLinePositionMap.put(label, linePosition);
    }

    @Override
    public boolean hasJumpLabelMapping(String label) {
        checkArgumentNotEmpty(label, "Label");
        return this.jumpLabelToLinePositionMap.containsKey(label);
    }

    @Override
    public Program build() {
        return new Program(this.instructionDescriptors, this.jumpLabelToLinePositionMap);
    }

}
