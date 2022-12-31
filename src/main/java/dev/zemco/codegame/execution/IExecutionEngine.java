package dev.zemco.codegame.execution;

import dev.zemco.codegame.compilation.IInstructionDescriptor;

import java.util.Optional;

public interface IExecutionEngine {
    void jumpTo(String label);
    Optional<IInstructionDescriptor> getNextInstructionDescriptor();
    void step();
}
