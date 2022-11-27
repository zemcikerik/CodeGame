package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class FactorySingleParameterInstructionParser extends AbstractSingleParameterInstructionParser {

    private final IStringInstructionFactory instructionFactory;

    public FactorySingleParameterInstructionParser(
        String instructionPrefix,
        IStringInstructionFactory instructionFactory
    ) {
        super(instructionPrefix);
        this.instructionFactory = checkArgumentNotNull(instructionFactory, "Instruction factory");
    }

    @Override
    protected IInstruction parseInstructionWithParameter(String parameter) {
        return this.instructionFactory.createInstruction(parameter);
    }

    @FunctionalInterface
    public interface IStringInstructionFactory {
        IInstruction createInstruction(String parameter);
    }

}
