package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.engine.NoNextInstructionException;
import dev.zemco.codegame.execution.engine.StepExecutionException;
import dev.zemco.codegame.problems.ProblemCase;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Implementation of a {@link ISolutionEvaluator solution evaluator} that evaluates an underlying
 * {@link IExecutionContext execution context} against a specific {@link ProblemCase problem case} using
 * an {@link IEvaluationStrategy evaluation strategy}.
 *
 * @author Erik Zemčík
 * @see IEvaluationStrategy
 */
public class SolutionEvaluator implements ISolutionEvaluator {

    private final IExecutionContext executionContext;
    private final IEvaluationStrategy evaluationStrategy;
    private final ProblemCase problemCase;

    private boolean errored;
    private boolean successful;

    /**
     * Creates an instance of {@link SolutionEvaluator}.
     *
     * @param executionContext execution context to evaluate
     * @param evaluationStrategy evaluation strategy to use for evaluation
     * @param problemCase problem case to evaluate against
     *
     * @throws IllegalArgumentException if any parameter is {@code null}
     */
    public SolutionEvaluator(
        IExecutionContext executionContext,
        IEvaluationStrategy evaluationStrategy,
        ProblemCase problemCase
    ) {
        this.executionContext = checkArgumentNotNull(executionContext, "Execution context");
        this.evaluationStrategy = checkArgumentNotNull(evaluationStrategy, "Evaluation strategy");
        this.problemCase = checkArgumentNotNull(problemCase, "Problem case");

        this.errored = false;
        this.successful = evaluationStrategy.evaluateSolutionForProblemCase(executionContext, problemCase);
    }

    @Override
    public boolean hasFinished() {
        return this.errored || this.successful;
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
        if (this.hasFinished()) {
            throw new IllegalStateException("Evaluation has already reached a result!");
        }

        try {
            this.executionContext.getExecutionEngine().step();
        } catch (NoNextInstructionException | StepExecutionException e) {
            this.errored = true;
            throw new StepEvaluationException("An exception occurred during an evaluation step!", e);
        }

        if (this.evaluationStrategy.evaluateSolutionForProblemCase(this.executionContext, this.problemCase)) {
            this.successful = true;
        }
    }

}
