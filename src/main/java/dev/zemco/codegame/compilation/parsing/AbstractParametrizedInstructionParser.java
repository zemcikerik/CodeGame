package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;

public abstract class AbstractParametrizedInstructionParser implements IInstructionParser {

    private final String instructionPrefix;

    protected AbstractParametrizedInstructionParser(String instructionPrefix) {
        this.instructionPrefix = checkArgumentNotEmpty(instructionPrefix, "Instruction prefix");
    }

    protected abstract IInstruction parseInstructionWithParameters(String[] parameters);

    @Override
    public boolean canParseInstruction(String instructionLine) {
        return instructionLine.startsWith(this.instructionPrefix);
    }

    @Override
    public IInstruction parseInstruction(String instructionLine) {
        // remove the instruction prefix
        String parameterPartWithWhitespace = instructionLine.substring(this.instructionPrefix.length());
        String parameterPart = parameterPartWithWhitespace.trim();

        if (parameterPart.isEmpty()) {
            // instruction line does not contain any parameters
            return this.parseInstructionWithParameters(new String[0]);
        }

        // split into parameters separated by whitespace of 1 to n length
        String[] parameters = parameterPart.split("\\s+");
        return this.parseInstructionWithParameters(parameters);
    }

    protected String getInstructionPrefix() {
        return this.instructionPrefix;
    }

}
