package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.programs.Program;
import dev.zemco.codegame.problems.Problem;
import dev.zemco.codegame.problems.ProblemCase;

public interface IEvaluationService {
    boolean evaluateSolutionOnAllCases(Program solution, Problem problem);
    ISolutionEvaluator getEvaluatorForSolutionAttempt(Program solution, ProblemCase problemCase);
}
