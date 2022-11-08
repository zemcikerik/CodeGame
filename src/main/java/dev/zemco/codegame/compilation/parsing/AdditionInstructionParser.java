package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.Instruction;
import dev.zemco.codegame.execution.instructions.AdditionInstruction;

// TODO: any amount of whitespace?
public class AdditionInstructionParser implements InstructionParser {

    @Override
    public boolean canParseInstruction(String instructionLine) {
        return instructionLine.startsWith("add");
    }

    // TODO: throw proper exceptions
    @Override
    public Instruction parseInstruction(String instructionLine) {
        String rawAddend = instructionLine.substring("add ".length());
        int addend = Integer.parseInt(rawAddend);
        return new AdditionInstruction(addend);
    }

}
