package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.NoNextInstructionException;
import dev.zemco.codegame.execution.StepExecutionException;
import dev.zemco.codegame.problems.ProblemCase;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class SolutionEvaluator implements ISolutionEvaluator {

    private final IExecutionContext executionContext;
    private final IEvaluationStrategy evaluationStrategy;
    private final ProblemCase problemCase;

    private boolean hasErrored;
    private boolean successful;

    public SolutionEvaluator(
        IExecutionContext executionContext,
        IEvaluationStrategy evaluationStrategy,
        ProblemCase problemCase
    ) {
        this.executionContext = checkArgumentNotNull(executionContext, "Execution context");
        this.evaluationStrategy = checkArgumentNotNull(evaluationStrategy, "Evaluation strategy");
        this.problemCase = checkArgumentNotNull(problemCase, "Problem case");

        this.hasErrored = false;
        this.successful = false;
    }

    @Override
    public boolean canContinue() {
        return !this.hasErrored && !this.successful;
    }

    @Override
    public boolean isSuccessful() {
        return this.successful;
    }

    @Override
    public IExecutionContext getExecutionContext() {
        return this.executionContext;
    }

    @Override
    public void step() {
        if (!this.canContinue()) {
            throw new IllegalStateException("Evaluation has already reached a result!");
        }

        try {
            this.executionContext.getExecutionEngine().step();
        } catch (NoNextInstructionException | StepExecutionException e) {
            this.hasErrored = true;
            throw e;
        }

        if (this.evaluationStrategy.evaluateSolutionForProblemCase(this.executionContext, this.problemCase)) {
            this.successful = true;
        }
    }

}
