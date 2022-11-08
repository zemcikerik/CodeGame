package dev.zemco.codegame.compilation;

public interface ProgramCompiler {
    Program compileProgram(String rawProgram) throws InvalidSyntaxException;
}
