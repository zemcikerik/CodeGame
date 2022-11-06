package dev.zemco.codegame.execution;

// TODO: javadoc

/**
 * Execution engine is an interface responsible for program execution.
 * It works in steps, each step executes an instruction at a given position of a program.
 * @author Erik Zemčík
 */
public interface ExecutionEngine {

    void step();

}
