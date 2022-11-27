package dev.zemco.codegame.problems;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;

/**
 * Problem cases memory settings contains memory settings, which are used to configure execution memory
 * during validation of player's solution.
 * Each memory configuration contains {@link #getSize() size} of the memory,
 * and {@link #getInitialValues() initial values}.
 * <p>
 * Memory settings are immutable, and may be deserialized from JSON thanks to bindings from Jackson library.
 *
 * @author Erik Zemčík
 * @see ProblemCase
 */
public class ProblemCaseMemorySettings {

    private final int size;
    private final Map<Integer, Integer> initialValues;

    /**
     * Constructs new instance of {@link ProblemCaseMemorySettings}.
     *
     * @param size size of the memory
     * @param initialValues initial values of the memory
     * @throws IllegalArgumentException if {@code size} is not a positive integer
     */
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

    /**
     * Returns size of the memory used during the evaluation of player's solution.
     * @return size of the memory
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Returns initial values of the memory used during the evaluation of player's solution.
     * @return initial values of the memory
     */
    public Map<Integer, Integer> getInitialValues() {
        return this.initialValues;
    }

}
