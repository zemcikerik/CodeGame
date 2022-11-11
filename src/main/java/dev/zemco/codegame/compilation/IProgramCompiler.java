package dev.zemco.codegame.compilation;

public interface IProgramCompiler {
    Program compileProgram(String rawProgram) throws InvalidSyntaxException;
}
