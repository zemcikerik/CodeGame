package dev.zemco.codegame.compilation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class Program {

    private final List<InstructionDescriptor> instructionDescriptors;
    private final Map<String, Integer> jumpLabelToPositionMap;

    public Program(List<InstructionDescriptor> instructionDescriptors, Map<String, Integer> jumpLabelToPositionMap) {
        checkArgumentNotNull(instructionDescriptors, "Instruction descriptors");
        checkArgumentNotNull(jumpLabelToPositionMap, "Jump label to position map");

        this.instructionDescriptors = Collections.unmodifiableList(instructionDescriptors);
        this.jumpLabelToPositionMap = Collections.unmodifiableMap(jumpLabelToPositionMap);
    }

    public List<InstructionDescriptor> getInstructionDescriptors() {
        return this.instructionDescriptors;
    }

    public Map<String, Integer> getJumpLabelToPositionMap() {
        return this.jumpLabelToPositionMap;
    }

}
