package dev.zemco.codegame.problems;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;
import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

/**
 * Problem is a challenge solvable by player.
 * Each problem consists of its {@link #getId() unique id}, {@link #getName() name},
 * {@link #getDescription() description}, {@link #getDifficulty() difficulty}, and {@link #getCases() problem cases}.
 * <p>
 * Problems are immutable, and may be deserialized from JSON thanks to bindings from Jackson library.
 *
 * @author Erik Zemčík
 * @see ProblemCase
 */
public class Problem {

    private final UUID id;
    private final String name;
    private final String description;
    private final int difficulty;
    private final List<ProblemCase> cases;

    /**
     * Constructs new instance of {@link Problem}.
     *
     * @param id unique identifier of the problem
     * @param name name of the problem
     * @param description description of the problem
     * @param difficulty difficulty of the problem
     * @param cases {@link List list} of {@link ProblemCase problem cases} for validating problem's solution
     * @throws IllegalArgumentException if any parameter is null, if {@code name} or {@code cases} are null,
     *                                  if {@code difficulty} is not positive
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Problem(
        @JsonProperty("id") UUID id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("difficulty") int difficulty,
        @JsonProperty("cases") List<ProblemCase> cases
    ) {
        this.id = checkArgumentNotNull(id, "Id");
        this.name = checkArgumentNotEmpty(name, "Name");
        this.description = checkArgumentNotNull(description, "Description");
        this.difficulty = checkArgumentPositiveInteger(difficulty, "Difficulty");
        this.cases = List.copyOf(checkArgumentNotEmpty(cases, "Cases"));
    }

    /**
     * Returns unique identifier associated with this problem.
     * @return unique identifier
     */
    public UUID getId() {
        return this.id;
    }

    /**
     * Returns name of this problem.
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns description of this problem.
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns positive difficulty of this problem.
     * @return difficulty
     */
    public int getDifficulty() {
        return this.difficulty;
    }

    /**
     * Returns {@link ProblemCase problem cases} used for validation of this problem's solution.
     * @return unmodifiable {@link List list} of {@link ProblemCase problem cases}
     */
    public List<ProblemCase> getCases() {
        return this.cases;
    }

    /**
     * Returns formatted string identifying this problem. This string is not guaranteed to be unique.
     * @return non-unique string identifying this problem
     */
    @Override
    public String toString() {
        return String.format("%s (%d ☆)", this.name, this.difficulty);
    }

}
