package dev.zemco.codegame.compilation;

import dev.zemco.codegame.programs.Program;

/**
 * Compiler takes a source code and compiles it into an executable {@link Program program}.
 * Syntax of the source code is dependent on the implementation.
 *
 * @author Erik Zemčík
 * @see Program
 */
public interface IProgramCompiler {

    /**
     * Compiles given source code into an executable {@link Program program}.
     *
     * @param sourceCode source code of the {@link Program program}
     * @return executable {@link Program program}
     *
     * @throws IllegalArgumentException if {@code sourceCode} is {@code null}
     * @throws InvalidSyntaxException if {@code sourceCode} contains invalid syntax to the implementation
     */
    Program compileProgram(String sourceCode) throws InvalidSyntaxException;

}
