package dev.zemco.codegame.execution.memory;

import dev.zemco.codegame.problems.ProblemCaseMemorySettings;

public interface IMemoryService {
    IMemory getConfiguredMemory(ProblemCaseMemorySettings memorySettings);
}
