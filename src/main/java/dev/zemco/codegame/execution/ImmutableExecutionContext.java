package dev.zemco.codegame.execution;

import dev.zemco.codegame.execution.io.InputSource;
import dev.zemco.codegame.execution.io.OutputSink;
import dev.zemco.codegame.execution.memory.Memory;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class ImmutableExecutionContext implements ExecutionContext {

    private final ExecutionEngine engine;
    private final Memory memory;
    private final InputSource inputSource;
    private final OutputSink outputSink;

    public ImmutableExecutionContext(ExecutionEngine engine, Memory memory, InputSource inputSource, OutputSink outputSink) {
        this.engine = checkArgumentNotNull(engine, "Engine");
        this.memory = checkArgumentNotNull(memory, "Memory");
        this.inputSource = checkArgumentNotNull(inputSource, "Input source");
        this.outputSink = checkArgumentNotNull(outputSink, "Output sink");
    }

    @Override
    public ExecutionEngine getExecutionEngine() {
        return this.engine;
    }

    @Override
    public Memory getMemory() {
        return this.memory;
    }

    @Override
    public InputSource getInputSource() {
        return this.inputSource;
    }

    @Override
    public OutputSink getOutputSink() {
        return this.outputSink;
    }

}
