package dev.zemco.codegame.execution;

import dev.zemco.codegame.execution.io.IInputSource;
import dev.zemco.codegame.execution.io.IOutputSink;
import dev.zemco.codegame.execution.memory.IMemory;

/**
 * Context of a single execution, which provides access to all components related to the execution.
 * @author Erik Zemčík
 */
public interface IExecutionContext {

    /**
     * Returns the execution engine of the execution.
     * @return execution engine
     */
    IExecutionEngine getExecutionEngine();

    /**
     * Returns the memory of the execution.
     * @return memory
     */
    IMemory getMemory();

    /**
     * Returns the input source of the execution.
     * @return input source
     */
    IInputSource getInputSource();

    /**
     * Returns the output sink of the execution.
     * @return output sink
     */
    IOutputSink getOutputSink();

}
