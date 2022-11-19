package dev.zemco.codegame.execution;

import dev.zemco.codegame.compilation.Program;
import dev.zemco.codegame.execution.io.IInputSource;
import dev.zemco.codegame.execution.io.IInputSourceFactory;
import dev.zemco.codegame.execution.io.IOutputSink;
import dev.zemco.codegame.execution.io.IOutputSinkFactory;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryService;
import dev.zemco.codegame.problems.ProblemCase;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class CodeExecutionService implements IExecutionService {

    private final IMemoryService memoryService;
    private final IInputSourceFactory inputSourceFactory;
    private final IOutputSinkFactory outputSinkFactory;

    public CodeExecutionService(
            IMemoryService memoryService,
            IInputSourceFactory inputSourceFactory,
            IOutputSinkFactory outputSinkFactory
    ) {
        this.memoryService = checkArgumentNotNull(memoryService, "Memory service");
        this.inputSourceFactory = checkArgumentNotNull(inputSourceFactory, "Input source factory");
        this.outputSinkFactory = checkArgumentNotNull(outputSinkFactory, "Output source factory");
    }

    @Override
    public IExecutionContext getExecutionContextForSolutionAttempt(ProblemCase problemCase, Program solution) {
        checkArgumentNotNull(problemCase, "Problem case");
        checkArgumentNotNull(solution, "Solution");

        IMemory memory = this.memoryService.getConfiguredMemory(problemCase.getMemorySettings());
        IInputSource inputSource = this.inputSourceFactory.createInputSourceFromIterable(problemCase.getInputs());
        IOutputSink outputSink = this.outputSinkFactory.createVerifyingOutputSinkFromIterable(problemCase.getExpectedOutputs());

        CodeExecutionEngine engine = new CodeExecutionEngine(solution, memory, inputSource, outputSink);
        return engine.getExecutionContext();
    }

}
