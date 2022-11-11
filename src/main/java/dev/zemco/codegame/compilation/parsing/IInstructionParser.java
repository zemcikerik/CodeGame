package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

public interface IInstructionParser {
    boolean canParseInstruction(String instructionLine);
    IInstruction parseInstruction(String instructionLine);
}
