package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.programs.Program;
import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.IExecutionService;
import dev.zemco.codegame.execution.engine.NoNextInstructionException;
import dev.zemco.codegame.execution.engine.StepExecutionException;
import dev.zemco.codegame.problems.Problem;
import dev.zemco.codegame.problems.ProblemCase;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class EvaluationService implements IEvaluationService {

    private final IExecutionService executionService;
    private final IEvaluationStrategy evaluationStrategy;

    public EvaluationService(IExecutionService executionService, IEvaluationStrategy evaluationStrategy) {
        this.executionService = checkArgumentNotNull(executionService, "Execution service");
        this.evaluationStrategy = checkArgumentNotNull(evaluationStrategy, "Evaluation strategy");
    }

    @Override
    public boolean evaluateSolutionOnAllCases(Program solution, Problem problem) {
        return problem.getCases().stream()
            .allMatch(problemCase -> this.evaluateSolution(solution, problemCase));
    }

    private boolean evaluateSolution(Program solution, ProblemCase problemCase) {
        ISolutionEvaluator evaluator = this.getEvaluatorForSolutionAttempt(solution, problemCase);

        while (evaluator.canContinue()) {
            try {
                evaluator.step();
            } catch (StepExecutionException | NoNextInstructionException e) {
                return false;
            }
        }

        return evaluator.isSuccessful();
    }

    @Override
    public ISolutionEvaluator getEvaluatorForSolutionAttempt(Program solution, ProblemCase problemCase) {
        checkArgumentNotNull(solution, "Solution");
        checkArgumentNotNull(problemCase, "Problem case");

        IExecutionContext executionContext = this.executionService.getExecutionContextForProblemCaseSolution(solution, problemCase);
        return new SolutionEvaluator(executionContext, this.evaluationStrategy, problemCase);
    }

}
