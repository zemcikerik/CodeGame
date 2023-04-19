package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.programs.Program;
import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.IExecutionService;
import dev.zemco.codegame.problems.Problem;
import dev.zemco.codegame.problems.ProblemCase;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Implementation of business logic related to evaluation of solutions of {@link Problem problems}.
 * This implementation uses an {@link IEvaluationStrategy evaluation strategy} for the evaluation,
 * and the {@link SolutionEvaluator} for controlling the flow of the evaluation.
 *
 * @author Erik Zemčík
 * @see IEvaluationStrategy
 */
public class EvaluationService implements IEvaluationService {

    private static final int MAXIMUM_STEP_COUNT_UNTIL_TIMEOUT = 1_000_000;
    private final IExecutionService executionService;
    private final IEvaluationStrategy evaluationStrategy;

    /**
     * Creates an instance of {@link EvaluationService}.
     *
     * @param executionService execution service for creating the underlying executions to be evaluated
     * @param evaluationStrategy evaluation strategy to use for evaluation
     *
     * @throws IllegalArgumentException if {@code executionService} is {@code null} or
     *                                  if {@code evaluationStrategy} is {@code null}
     */
    public EvaluationService(IExecutionService executionService, IEvaluationStrategy evaluationStrategy) {
        this.executionService = checkArgumentNotNull(executionService, "Execution service");
        this.evaluationStrategy = checkArgumentNotNull(evaluationStrategy, "Evaluation strategy");
    }

    @Override
    public boolean evaluateSolutionOnAllProblemCases(Program solution, Problem problem) {
        checkArgumentNotNull(solution, "Solution");
        checkArgumentNotNull(problem, "Problem");

        return problem.getCases().stream()
            .allMatch(problemCase -> this.evaluateSolution(solution, problemCase));
    }

    private boolean evaluateSolution(Program solution, ProblemCase problemCase) {
        ISolutionEvaluator evaluator = this.getEvaluatorForProblemCaseSolution(solution, problemCase);

        while (!evaluator.hasFinished()) {
            try {
                evaluator.step();
            } catch (StepEvaluationException e) {
                return false;
            }
        }

        return evaluator.isSuccessful();
    }

    @Override
    public ISolutionEvaluator getEvaluatorForProblemCaseSolution(Program solution, ProblemCase problemCase) {
        checkArgumentNotNull(solution, "Solution");
        checkArgumentNotNull(problemCase, "Problem case");

        IExecutionContext executionContext = this.executionService.getExecutionContextForProblemCaseSolution(solution, problemCase);

        return new TimeoutSolutionEvaluatorDecorator(
            new SolutionEvaluator(executionContext, this.evaluationStrategy, problemCase),
            MAXIMUM_STEP_COUNT_UNTIL_TIMEOUT
        );
    }

}
