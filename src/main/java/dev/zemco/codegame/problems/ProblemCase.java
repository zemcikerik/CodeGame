package dev.zemco.codegame.problems;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class ProblemCase {

    private final List<Integer> inputs;
    private final List<Integer> expectedOutputs;
    private final ProblemCaseMemorySettings memorySettings;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ProblemCase(
            @JsonProperty("inputs") List<Integer> inputs,
            @JsonProperty("expectedOutputs") List<Integer> expectedOutputs,
            @JsonProperty("memorySettings") ProblemCaseMemorySettings memorySettings
    ) {
        if (expectedOutputs == null) {
            throw new IllegalArgumentException("Expected outputs cannot be null!");
        }

        if (expectedOutputs.isEmpty()) {
            throw new IllegalArgumentException("Expected outputs cannot be empty!");
        }

        if (memorySettings == null) {
            throw new IllegalArgumentException("Memory settings cannot be null!");
        }

        this.inputs = inputs != null ? Collections.unmodifiableList(inputs) : Collections.emptyList();
        this.expectedOutputs = Collections.unmodifiableList(expectedOutputs);
        this.memorySettings = memorySettings;
    }

    public List<Integer> getInputs() {
        return this.inputs;
    }

    public List<Integer> getExpectedOutputs() {
        return this.expectedOutputs;
    }

    public ProblemCaseMemorySettings getMemorySettings() {
        return this.memorySettings;
    }

}
