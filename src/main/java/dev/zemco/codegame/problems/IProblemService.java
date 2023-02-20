package dev.zemco.codegame.problems;

import java.util.List;

/**
 * Service that manages business logic related to management of problems available to the application.
 * @author Erik Zemčík
 */
public interface IProblemService {

    /**
     * Returns all problems available to the application.
     * @return unmodifiable list of problems
     */
    List<Problem> getAllProblems();

}
