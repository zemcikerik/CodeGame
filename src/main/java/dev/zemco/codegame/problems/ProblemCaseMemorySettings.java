package dev.zemco.codegame.problems;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;

public class ProblemCaseMemorySettings {

    private final int size;
    private final Map<Integer, Integer> initialValues;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ProblemCaseMemorySettings(
            @JsonProperty("size") int size,
            @JsonProperty("initialValues") Map<Integer, Integer> initialValues
    ) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be a positive integer!");
        }

        this.size = size;
        this.initialValues = initialValues != null ? Map.copyOf(initialValues) : Collections.emptyMap();
    }

    public int getSize() {
        return this.size;
    }

    public Map<Integer, Integer> getInitialValues() {
        return this.initialValues;
    }

}
