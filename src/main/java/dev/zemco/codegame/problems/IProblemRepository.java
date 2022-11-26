package dev.zemco.codegame.problems;

import java.util.List;

/**
 * Handles manipulation and storage of {@link Problem problems} used by application.
 *
 * @author Erik Zemčík
 * @see Problem
 */
public interface IProblemRepository {

    /**
     * Returns all known {@link Problem problems}.
     * @return all known {@link Problem problems}
     */
    List<Problem> getAllProblems();

}
