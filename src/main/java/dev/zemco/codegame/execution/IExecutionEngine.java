package dev.zemco.codegame.execution;

public interface IExecutionEngine {
    void jumpTo(String label);
    void step();
}
