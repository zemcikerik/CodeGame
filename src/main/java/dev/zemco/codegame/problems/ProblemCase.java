package dev.zemco.codegame.problems;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;

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
        this.inputs = inputs != null ? Collections.unmodifiableList(inputs) : Collections.emptyList();
        this.expectedOutputs = Collections.unmodifiableList(
            checkArgumentNotEmpty(expectedOutputs, "Expected outputs")
        );
        this.memorySettings = checkArgumentNotNull(memorySettings, "Memory settings");
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
