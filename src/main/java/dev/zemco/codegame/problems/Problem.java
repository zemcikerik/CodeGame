package dev.zemco.codegame.problems;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

// TODO: null checks
public class Problem {

    // TODO: uuid?
    private final UUID id;
    private final String name;
    private final String description;
    private final int rating;
    private final List<ProblemCase> cases;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Problem(
            @JsonProperty("id") UUID id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("rating") int rating,
            @JsonProperty("cases") List<ProblemCase> cases
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.cases = cases;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getRating() {
        return this.rating;
    }

    public List<ProblemCase> getCases() {
        return this.cases;
    }

}
