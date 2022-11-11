package dev.zemco.codegame.execution;

import dev.zemco.codegame.execution.io.IInputSource;
import dev.zemco.codegame.execution.io.IOutputSink;
import dev.zemco.codegame.execution.memory.IMemory;

// TODO: javadoc

public interface IExecutionContext {
    IExecutionEngine getExecutionEngine();
    IMemory getMemory();
    IInputSource getInputSource();
    IOutputSink getOutputSink();
}
