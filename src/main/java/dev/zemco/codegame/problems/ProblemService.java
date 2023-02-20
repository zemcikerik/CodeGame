package dev.zemco.codegame.problems;

import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Implementation of business logic related to management of problems available to the application that uses
 * provided problem repository as source of problems.
 * <p>
 * Currently, this service only serves as isolation between data access layer and presentation layer.
 *
 * @author Erik Zemčík
 */
public class ProblemService implements IProblemService {

    private final IProblemRepository problemRepository;

    /**
     * Creates an instance of {@link ProblemService}.
     * @param problemRepository problem repository to use as source of problems
     * @throws IllegalArgumentException if {@code problemRepository} is {@code null}
     */
    public ProblemService(IProblemRepository problemRepository) {
        this.problemRepository = checkArgumentNotNull(problemRepository, "Problem repository");
    }

    @Override
    public List<Problem> getAllProblems() {
        return this.problemRepository.getAllProblems();
    }

}
