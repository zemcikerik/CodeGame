package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.problems.ProblemCase;

public class CodeEvaluationStrategy implements IEvaluationStrategy {

    @Override
    public boolean evaluateSolutionForProblemCase(IExecutionContext executionContext, ProblemCase problemCase) {
        return executionContext.getOutputSink().isSatisfied();
    }

}
