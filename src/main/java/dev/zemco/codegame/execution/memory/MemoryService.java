package dev.zemco.codegame.execution.memory;

import dev.zemco.codegame.problems.ProblemCaseMemorySettings;

import java.util.Map.Entry;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class MemoryService implements IMemoryService {

    private final IMemoryFactory memoryFactory;

    public MemoryService(IMemoryFactory memoryFactory) {
        this.memoryFactory = checkArgumentNotNull(memoryFactory, "Memory factory");
    }

    @Override
    public IMemory getConfiguredMemory(ProblemCaseMemorySettings memorySettings) {
        checkArgumentNotNull(memorySettings, "Memory settings");
        IMemory memory = this.memoryFactory.createMemoryWithSize(memorySettings.getSize());

        // set memory cells to initial values
        for (Entry<Integer, Integer> entry : memorySettings.getInitialValues().entrySet()) {
            int address = entry.getKey();
            int value = entry.getValue();
            memory.getCellByAddress(address).setValue(value);
        }

        return memory;
    }

}
