package dev.zemco.codegame.compilation;

import dev.zemco.codegame.execution.instructions.Instruction;
import dev.zemco.codegame.util.Preconditions;

public class ImmutableInstructionDescriptor implements InstructionDescriptor {

    private final Instruction instruction;
    private final int linePosition;

    public ImmutableInstructionDescriptor(Instruction instruction, int linePosition) {
        this.instruction = Preconditions.checkArgumentNotNull(instruction, "Instruction");
        // TODO: check line position
        this.linePosition = linePosition;
    }

    @Override
    public Instruction getInstruction() {
        return this.instruction;
    }

    @Override
    public int getLinePosition() {
        return this.linePosition;
    }

}
