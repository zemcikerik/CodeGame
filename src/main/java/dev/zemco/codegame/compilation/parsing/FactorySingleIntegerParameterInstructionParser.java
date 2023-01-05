package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Parses a named {@link IInstruction instruction} with a single integer parameter by requesting it
 * from a backing {@link IIntegerInstructionFactory factory}.
 * The backing {@link IIntegerInstructionFactory factory} cannot be modified after construction.
 * <p>
 * This class is implemented by wrapping the given backing factory with a string based one.
 *
 * @author Erik Zemčík
 * @see IIntegerInstructionFactory
 */
public class FactorySingleIntegerParameterInstructionParser extends FactorySingleParameterInstructionParser {

    /**
     * Creates an instance of {@link FactorySingleIntegerParameterInstructionParser} that parses
     * {@link IInstruction instructions} with the given name by requesting them from the given backing
     * {@link IIntegerInstructionFactory factory}.
     *
     * @param instructionName name of the raw {@link IInstruction instruction}
     * @param instructionFactory source of parsed {@link IInstruction instructions}
     *
     * @throws IllegalArgumentException if {@code instructionName} is {@code null} or empty or
     *                                  if {@code instructionFactory} is {@code null}
     */
    public FactorySingleIntegerParameterInstructionParser(
        String instructionName,
        IIntegerInstructionFactory instructionFactory
    ) {
        super(instructionName, wrapWithParsingStringFactory(instructionFactory));
    }

    private static IStringInstructionFactory wrapWithParsingStringFactory(IIntegerInstructionFactory instructionFactory) {
        checkArgumentNotNull(instructionFactory, "Instruction factory");
        return stringParameter -> instructionFactory.createInstruction(parseParameter(stringParameter));
    }

    private static int parseParameter(String parameter) {
        try {
            return Integer.parseInt(parameter);
        } catch (NumberFormatException e) {
            throw new InstructionParseException("Parameter is not a valid integer!", e);
        }
    }

    /**
     * Produces {@link IInstruction instructions} from a single {@link Integer integer} parameter.
     * @author Erik Zemčík
     */
    public interface IIntegerInstructionFactory {

        /**
         * Creates an {@link IInstruction instruction} ready for use.
         *
         * @param parameter raw {@link Integer integer} parameter representing the requested
         *                  {@link IInstruction instruction} state
         *
         * @return {@link IInstruction instruction} associated with the {@code parameter}
         * @throws InstructionParseException if the factory is unable to parse the {@link IInstruction instruction}
         */
        IInstruction createInstruction(int parameter);

    }

}
