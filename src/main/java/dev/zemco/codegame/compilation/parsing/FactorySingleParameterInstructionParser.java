package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.Instruction;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

// TODO: rename
public class FactorySingleParameterInstructionParser extends AbstractSingleParameterInstructionParser {

    private final StringInstructionFactory instructionFactory;

    public FactorySingleParameterInstructionParser(String instructionPrefix, StringInstructionFactory instructionFactory) {
        super(instructionPrefix);
        this.instructionFactory = checkArgumentNotNull(instructionFactory, "Instruction factory");
    }

    @Override
    protected Instruction parseInstructionWithParameter(String parameter) {
        return this.instructionFactory.createInstruction(parameter);
    }

    @FunctionalInterface
    public interface StringInstructionFactory {
        Instruction createInstruction(String parameter);
    }

}
