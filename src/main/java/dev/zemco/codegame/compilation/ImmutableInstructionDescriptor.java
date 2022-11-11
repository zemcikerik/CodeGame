package dev.zemco.codegame.compilation;

import dev.zemco.codegame.execution.instructions.IInstruction;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class ImmutableInstructionDescriptor implements IInstructionDescriptor {

    private final IInstruction instruction;
    private final int linePosition;

    public ImmutableInstructionDescriptor(IInstruction instruction, int linePosition) {
        this.instruction = checkArgumentNotNull(instruction, "Instruction");
        // TODO: check line position
        this.linePosition = linePosition;
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
