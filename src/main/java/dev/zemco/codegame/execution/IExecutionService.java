package dev.zemco.codegame.execution;

import dev.zemco.codegame.compilation.Program;
import dev.zemco.codegame.problems.ProblemCase;

/**
 * Service that manages business logic related to execution.
 * @author Erik Zemčík
 */
public interface IExecutionService {

    /**
     * Provides an {@link IExecutionContext execution context} configured to execute
     * the solution {@link Program program} for a given {@link ProblemCase problem case}.
     *
     * @param solution program to execute
     * @param problemCase problem case to execute against
     * @return configured execution context
     *
     * @throws IllegalArgumentException if {@code solution} is {@code null} or if {@code problemCase} is {@code null}
     */
    IExecutionContext getExecutionContextForProblemCaseSolution(Program solution, ProblemCase problemCase);

}
