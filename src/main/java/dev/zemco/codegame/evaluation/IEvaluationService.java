package dev.zemco.codegame.evaluation;

import dev.zemco.codegame.programs.Program;
import dev.zemco.codegame.problems.Problem;
import dev.zemco.codegame.problems.ProblemCase;

/**
 * Service that manages business logic related to evaluation of solutions of {@link Problem problems}
 * created by the player.
 *
 * @author Erik Zemčík
 */
public interface IEvaluationService {

    /**
     * Evaluates player's solution {@link Program program} against all of the {@link ProblemCase problem cases}
     * of the given {@link Problem problem}.
     *
     * @param solution solution program to evaluate
     * @param problem problem to evaluate against
     * @return true if the solution has passed all the problems cases, else false
     *
     * @throws IllegalArgumentException if {@code solution} is {@code null} or if {@code problem} is {@code null}
     */
    boolean evaluateSolutionOnAllProblemCases(Program solution, Problem problem);

    /**
     * Provides an {@link ISolutionEvaluator evalutor} for the solution {@link Program program} against
     * the given specific {@link ProblemCase problem case}. This approach is recommended when the state
     * of the underlying execution needs to be observed during the evaluation.
     *
     * @param solution solution program to evaluate
     * @param problemCase problem case to evaluate against
     * @return evaluator for the given problem case
     *
     * @throws IllegalArgumentException if {@code solution} is {@code null} or if {@code problemCase} is {@code null}
     */
    ISolutionEvaluator getEvaluatorForProblemCaseSolution(Program solution, ProblemCase problemCase);

}
