package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.Instruction;

public interface InstructionParser {
    boolean canParseInstruction(String instructionLine);
    Instruction parseInstruction(String instructionLine);
}
