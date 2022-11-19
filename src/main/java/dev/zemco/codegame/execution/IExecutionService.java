package dev.zemco.codegame.execution;

import dev.zemco.codegame.compilation.Program;
import dev.zemco.codegame.problems.ProblemCase;

public interface IExecutionService {
    IExecutionContext getExecutionContextForSolutionAttempt(ProblemCase problemCase, Program solution);
}
