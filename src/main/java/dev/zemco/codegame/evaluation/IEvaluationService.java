package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.compilation.Program;
import dev.zemco.codegame.problems.ProblemCase;

public interface IEvaluationService {
    ISolutionEvaluator getEvaluatorForSolutionAttempt(Program solution, ProblemCase problemCase);
}
