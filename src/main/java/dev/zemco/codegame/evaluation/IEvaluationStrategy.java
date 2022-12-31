package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.problems.ProblemCase;

public interface IEvaluationStrategy {
    boolean evaluateSolutionForProblemCase(IExecutionContext executionContext, ProblemCase problemCase);
}
