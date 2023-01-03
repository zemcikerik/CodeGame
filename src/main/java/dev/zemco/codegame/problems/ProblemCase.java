package dev.zemco.codegame.problems;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;

/**
 * Problem case describes a test case of a {@link Problem problem}, which is used to validate player solution.
 * Each problem consists of {@link #getInputs() inputs}, {@link #getExpectedOutputs() expected outputs},
 * and {@link #getMemorySettings() memory settings}.
 * <p>
 * Problem cases are immutable, and may be deserialized from JSON thanks to bindings from Jackson library.
 *
 * @author Erik Zemčík
 * @see Problem
 * @see ProblemCaseMemorySettings
 */
public class ProblemCase {

    private final List<Integer> inputs;
    private final List<Integer> expectedOutputs;
    private final ProblemCaseMemorySettings memorySettings;

    /**
     * Creates an instance of {@link ProblemCase}.
     *
     * @param inputs {@link List list} of inputs used during validation
     * @param expectedOutputs {@link List list} of outputs expected during validation
     * @param memorySettings memory settings used during validation
     *
     * @throws IllegalArgumentException if {@code expectedOutputs} are null or empty, if {@code memorySettings} are null
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ProblemCase(
        @JsonProperty("inputs") List<Integer> inputs,
        @JsonProperty("expectedOutputs") List<Integer> expectedOutputs,
        @JsonProperty("memorySettings") ProblemCaseMemorySettings memorySettings
    ) {
        this.inputs = inputs != null ? List.copyOf(inputs) : Collections.emptyList();
        this.expectedOutputs = List.copyOf(checkArgumentNotEmpty(expectedOutputs, "Expected outputs"));
        this.memorySettings = checkArgumentNotNull(memorySettings, "Memory settings");
    }

    /**
     * Returns inputs that are available to player's solution during validation.
     * @return unmodifiable {@link List list} of inputs
     */
    public List<Integer> getInputs() {
        return this.inputs;
    }

    /**
     * Returns outputs that are expected from player's solution during validation.
     * @return unmodifiable {@link List list} of expected outputs
     */
    public List<Integer> getExpectedOutputs() {
        return this.expectedOutputs;
    }

    /**
     * Returns {@link ProblemCaseMemorySettings memory settings} which are used to configure memory during validation.
     * @return memory settings
     */
    public ProblemCaseMemorySettings getMemorySettings() {
        return this.memorySettings;
    }

}
