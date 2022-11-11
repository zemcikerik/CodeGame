package dev.zemco.codegame.compilation;

import dev.zemco.codegame.execution.instructions.IInstruction;

public interface IInstructionDescriptor {
    IInstruction getInstruction();
    int getLinePosition();
}
