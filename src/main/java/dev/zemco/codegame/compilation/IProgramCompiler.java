package dev.zemco.codegame.compilation;

public interface IProgramCompiler {
    Program compileProgram(String sourceCode) throws InvalidSyntaxException;
}
