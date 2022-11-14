package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

public abstract class AbstractSingleIntegerParameterInstructionParser extends AbstractSingleParameterInstructionParser {

    public AbstractSingleIntegerParameterInstructionParser(String instructionPrefix) {
        super(instructionPrefix);
    }

    protected abstract IInstruction parseInstructionWithIntegerParameter(int parameter);

    @Override
    protected IInstruction parseInstructionWithParameter(String parameter) {
        return this.parseInstructionWithIntegerParameter(this.parseInteger(parameter));
    }

    private int parseInteger(String parameter) {
        try {
            return Integer.parseInt(parameter);
        } catch (NumberFormatException e) {
            throw new ParseException("Parameter is not a valid integer!", e);
        }
    }

}
