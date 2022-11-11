package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.Instruction;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNullAndNotEmpty;

public abstract class AbstractParametrizedInstructionParser implements InstructionParser {

    private final String instructionPrefix;

    public AbstractParametrizedInstructionParser(String instructionPrefix) {
        this.instructionPrefix = checkArgumentNotNullAndNotEmpty(instructionPrefix, "Instruction prefix");
    }

    protected abstract Instruction parseInstructionWithParameters(String[] parameters);

    @Override
    public boolean canParseInstruction(String instructionLine) {
        return instructionLine.startsWith(this.instructionPrefix);
    }

    @Override
    public Instruction parseInstruction(String instructionLine) {
        // remove the instruction prefix
        String parameterPartWithWhitespace = instructionLine.substring(this.instructionPrefix.length());
        String parameterPart = parameterPartWithWhitespace.trim();

        // split into parameters separated by whitespace of 1 to n length
        String[] parameters = parameterPart.split("\\s+");
        return this.parseInstructionWithParameters(parameters);
    }

    protected String getInstructionPrefix() {
        return this.instructionPrefix;
    }

}
