package dev.zemco.codegame.compilation;

import dev.zemco.codegame.execution.instructions.IInstruction;

// TODO: maybe only use concrete immutable class and remove factory usage from ProgramCompiler
public interface IInstructionDescriptor {
    IInstruction getInstruction();
    int getLinePosition();
}
