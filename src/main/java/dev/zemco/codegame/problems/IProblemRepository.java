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
     * Returns all known {@link Problem problems}.
     * @return unmodifiable {@link List} of all {@link Problem problems}
     */
    List<Problem> getAllProblems();

}
