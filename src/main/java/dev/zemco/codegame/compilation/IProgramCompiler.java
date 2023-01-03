package dev.zemco.codegame.compilation;

import dev.zemco.codegame.programs.Program;

public interface IProgramCompiler {
    Program compileProgram(String sourceCode) throws InvalidSyntaxException;
}
