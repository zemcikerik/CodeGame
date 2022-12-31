package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.compilation.Program;
import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.IExecutionService;
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
    public ISolutionEvaluator getEvaluatorForSolutionAttempt(Program solution, ProblemCase problemCase) {
        checkArgumentNotNull(solution, "Solution");
        checkArgumentNotNull(problemCase, "Problem case");

        IExecutionContext executionContext = this.executionService.getExecutionContextForSolutionAttempt(solution, problemCase);
        return new SolutionEvaluator(executionContext, this.evaluationStrategy, problemCase);
    }

}
