package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

public abstract class AbstractSingleParameterInstructionParser extends AbstractParametrizedInstructionParser {

    public AbstractSingleParameterInstructionParser(String instructionPrefix) {
        super(instructionPrefix);
    }

    protected abstract IInstruction parseInstructionWithParameter(String parameter);

    @Override
    protected IInstruction parseInstructionWithParameters(String[] parameters) {
        if (parameters.length != 1) {
            // TODO: pretty this lmao
            throw new ParseException(String.format(
                "Instruction %s expected 1 parameter, found %d!",
                this.getInstructionPrefix(), parameters.length
            ));
        }

        return this.parseInstructionWithParameter(parameters[0]);
    }

}
