package dev.zemco.codegame.compilation;

import java.util.List;
import java.util.Map;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class Program {

    private final List<IInstructionDescriptor> instructionDescriptors;
    private final Map<String, Integer> jumpLabelToLinePositionMap;

    public Program(
        List<IInstructionDescriptor> instructionDescriptors,
        Map<String, Integer> jumpLabelToLinePositionMap
    ) {
        checkArgumentNotNull(instructionDescriptors, "Instruction descriptors");
        checkArgumentNotNull(jumpLabelToLinePositionMap, "Jump label to line position map");

        this.instructionDescriptors = List.copyOf(instructionDescriptors);
        this.jumpLabelToLinePositionMap = Map.copyOf(jumpLabelToLinePositionMap);
    }

    public List<IInstructionDescriptor> getInstructionDescriptors() {
        return this.instructionDescriptors;
    }

    public Map<String, Integer> getJumpLabelToLinePositionMap() {
        return this.jumpLabelToLinePositionMap;
    }

}
