package dev.zemco.codegame.compilation;

import dev.zemco.codegame.execution.instructions.Instruction;

public interface InstructionDescriptor {
    Instruction getInstruction();
    int getLinePosition();
}
