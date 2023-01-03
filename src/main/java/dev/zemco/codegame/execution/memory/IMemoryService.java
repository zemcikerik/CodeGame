package dev.zemco.codegame.execution.memory;

import dev.zemco.codegame.problems.ProblemCaseMemorySettings;

/**
 * Service that manages business logic related to {@link IMemory memory}.
 * @author Erik Zemčík
 */
public interface IMemoryService {

    /**
     * Provides a {@link IMemory memory} configured using the provided memory settings.
     * <p>
     * This memory can be used for execution and evaluation related to
     * the {@link dev.zemco.codegame.problems.ProblemCase problem case} that owns the provided memory settings.
     *
     * @param memorySettings settings to use to configure the memory
     * @return configured memory
     *
     * @throws IllegalArgumentException if {@code memorySettings} is {@code null}
     */
    IMemory getConfiguredMemory(ProblemCaseMemorySettings memorySettings);

}
