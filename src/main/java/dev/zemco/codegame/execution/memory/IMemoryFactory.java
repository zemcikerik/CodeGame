package dev.zemco.codegame.execution.memory;

@FunctionalInterface
public interface IMemoryFactory {
    IMemory createMemoryWithSize(int size);
}
