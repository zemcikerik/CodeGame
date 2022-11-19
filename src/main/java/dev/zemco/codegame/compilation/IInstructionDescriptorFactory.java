package dev.zemco.codegame.compilation;

import dev.zemco.codegame.execution.instructions.IInstruction;

@FunctionalInterface
public interface IInstructionDescriptorFactory {
    IInstructionDescriptor createInstructionDescriptor(IInstruction instruction, int linePosition);
}
