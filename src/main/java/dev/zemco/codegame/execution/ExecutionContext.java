package dev.zemco.codegame.execution;

import dev.zemco.codegame.execution.io.InputSource;
import dev.zemco.codegame.execution.io.OutputSink;
import dev.zemco.codegame.execution.memory.Memory;

// TODO: javadoc

public interface ExecutionContext {
    ExecutionEngine getExecutionEngine();
    Memory getMemory();
    InputSource getInputSource();
    OutputSink getOutputSink();
}
