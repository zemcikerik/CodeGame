package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.execution.IExecutionContext;

public interface ISolutionEvaluator {
    boolean canContinue();
    boolean isSuccessful();
    IExecutionContext getExecutionContext();
    void step();
}
