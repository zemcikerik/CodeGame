package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Parses a named {@link IInstruction instruction} with a single address parameter by requesting it
 * from a backing {@link IIntegerInstructionFactory factory}.
 * The backing {@link IIntegerInstructionFactory factory} cannot be modified after construction.
 * <p>
 * This class is implemented by wrapping the given backing factory with a validating integer based one.
 *
 * @author Erik Zemčík
 */
public class FactorySingleAddressParameterInstructionParser extends FactorySingleIntegerParameterInstructionParser {

    /**
     * Creates an instance of {@link FactorySingleAddressParameterInstructionParser} that parses
     * {@link IInstruction instructions} with the given name by requesting them from the given backing
     * {@link IIntegerInstructionFactory factory}.
     *
     * @param instructionName name of the raw {@link IInstruction instruction}
     * @param instructionFactory source of parsed {@link IInstruction instructions}
     *
     * @throws IllegalArgumentException if {@code instructionName} is {@code null} or empty or
     *                                  if {@code instructionFactory} is {@code null}
     */
    public FactorySingleAddressParameterInstructionParser(
        String instructionName,
        IIntegerInstructionFactory instructionFactory
    ) {
        super(instructionName, wrapWithValidatingFactory(instructionFactory));
    }

    private static IIntegerInstructionFactory wrapWithValidatingFactory(IIntegerInstructionFactory instructionFactory) {
        checkArgumentNotNull(instructionFactory, "Instruction factory");
        return parameter -> instructionFactory.createInstruction(validateParameter(parameter));
    }

    private static int validateParameter(int parameter) {
        if (parameter < 0) {
            throw new InstructionParseException("Address parameter must be a positive integer!");
        }
        return parameter;
    }

}
