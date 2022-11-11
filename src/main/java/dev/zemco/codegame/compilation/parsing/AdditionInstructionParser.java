package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;
import dev.zemco.codegame.execution.instructions.AdditionInstruction;

// TODO: any amount of whitespace?
public class AdditionInstructionParser implements IInstructionParser {

    @Override
    public boolean canParseInstruction(String instructionLine) {
        return instructionLine.startsWith("add");
    }

    // TODO: throw proper exceptions
    @Override
    public IInstruction parseInstruction(String instructionLine) {
        String rawAddend = instructionLine.substring("add ".length());
        int addend = Integer.parseInt(rawAddend);
        return new AdditionInstruction(addend);
    }

}
