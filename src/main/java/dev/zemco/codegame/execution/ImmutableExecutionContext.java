package dev.zemco.codegame.execution;

import dev.zemco.codegame.execution.io.IInputSource;
import dev.zemco.codegame.execution.io.IOutputSink;
import dev.zemco.codegame.execution.memory.IMemory;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Implementation of the {@link IExecutionContext execution context}, where individual components cannot be
 * changed during the execution.
 * @author Erik Zemčík
 */
public class ImmutableExecutionContext implements IExecutionContext {

    private final IExecutionEngine engine;
    private final IMemory memory;
    private final IInputSource inputSource;
    private final IOutputSink outputSink;

    /**
     * Creates an instance of {@link ImmutableExecutionContext}, which holds the provided components.
     *
     * @param engine engine of the execution
     * @param memory memory of the execution
     * @param inputSource input source of the execution
     * @param outputSink output sink of the execution
     * @throws IllegalArgumentException if any parameter is {@code null}
     */
    public ImmutableExecutionContext(
        IExecutionEngine engine,
        IMemory memory,
        IInputSource inputSource,
        IOutputSink outputSink
    ) {
        this.engine = checkArgumentNotNull(engine, "Engine");
        this.memory = checkArgumentNotNull(memory, "Memory");
        this.inputSource = checkArgumentNotNull(inputSource, "Input source");
        this.outputSink = checkArgumentNotNull(outputSink, "Output sink");
    }

    @Override
    public IExecutionEngine getExecutionEngine() {
        return this.engine;
    }

    @Override
    public IMemory getMemory() {
        return this.memory;
    }

    @Override
    public IInputSource getInputSource() {
        return this.inputSource;
    }

    @Override
    public IOutputSink getOutputSink() {
        return this.outputSink;
    }

}
