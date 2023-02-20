package dev.zemco.codegame.problems;

import java.util.List;

/**
 * Handles storage of {@link Problem problems} used by the application.
 *
 * @author Erik Zemčík
 * @see Problem
 */
public interface IProblemRepository {

    /**
     * Returns all known problems.
     * @return unmodifiable list of all problems
     */
    List<Problem> getAllProblems();

}
