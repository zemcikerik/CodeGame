package dev.zemco.codegame.execution;

import dev.zemco.codegame.compilation.Program;
import dev.zemco.codegame.execution.io.IInputSource;
import dev.zemco.codegame.execution.io.IInputSourceFactory;
import dev.zemco.codegame.execution.io.IOutputSink;
import dev.zemco.codegame.execution.io.IOutputSinkFactory;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryService;
import dev.zemco.codegame.problems.ProblemCase;

import java.util.List;

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
    public IExecutionContext getExecutionContextForSolutionAttempt(Program solution, ProblemCase problemCase) {
        checkArgumentNotNull(solution, "Solution");
        checkArgumentNotNull(problemCase, "Problem case");

        IMemory memory = this.createMemoryForProblemCase(problemCase);
        IInputSource inputSource = this.createInputSourceForProblemCase(problemCase);
        IOutputSink outputSink = this.createOutputSinkForProblemCase(problemCase);

        CodeExecutionEngine engine = new CodeExecutionEngine(solution, memory, inputSource, outputSink);
        return engine.getExecutionContext();
    }

    private IMemory createMemoryForProblemCase(ProblemCase problemCase) {
        return this.memoryService.getConfiguredMemory(problemCase.getMemorySettings());
    }

    private IInputSource createInputSourceForProblemCase(ProblemCase problemCase) {
        List<Integer> inputs = problemCase.getInputs();
        return this.inputSourceFactory.createInputSourceFromIterable(inputs);
    }

    private IOutputSink createOutputSinkForProblemCase(ProblemCase problemCase) {
        List<Integer> expectedOutputs = problemCase.getExpectedOutputs();
        return this.outputSinkFactory.createVerifyingOutputSinkFromIterable(expectedOutputs);
    }

}
