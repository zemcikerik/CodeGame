package dev.zemco.codegame.compilation;

import dev.zemco.codegame.execution.instructions.IInstruction;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

public class ImmutableInstructionDescriptor implements IInstructionDescriptor {

    private final IInstruction instruction;
    private final int linePosition;

    public ImmutableInstructionDescriptor(IInstruction instruction, int linePosition) {
        this.instruction = checkArgumentNotNull(instruction, "Instruction");
        this.linePosition = checkArgumentPositiveInteger(linePosition, "Line position");
    }

    @Override
    public IInstruction getInstruction() {
        return this.instruction;
    }

    @Override
    public int getLinePosition() {
        return this.linePosition;
    }

}
