package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class FactorySingleIntegerParameterInstructionParser extends AbstractSingleIntegerParameterInstructionParser {

    private final IIntegerInstructionFactory instructionFactory;

    public FactorySingleIntegerParameterInstructionParser(String instructionPrefix, IIntegerInstructionFactory instructionFactory) {
        super(instructionPrefix);
        this.instructionFactory = checkArgumentNotNull(instructionFactory, "Instruction factory");
    }

    @Override
    protected IInstruction parseInstructionWithIntegerParameter(int parameter) {
        return this.instructionFactory.createInstruction(parameter);
    }

    @FunctionalInterface
    public interface IIntegerInstructionFactory {
        IInstruction createInstruction(int parameter);
    }

}
