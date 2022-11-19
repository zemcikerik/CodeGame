package dev.zemco.codegame.execution;

/**
 * Execution engine is an interface responsible for program execution.
 * It works in steps, each step executes an instruction at a given position of a program.
 * @author Erik Zemčík
 */
public interface IExecutionEngine {

    void jumpTo(String label);

    void step();

}
