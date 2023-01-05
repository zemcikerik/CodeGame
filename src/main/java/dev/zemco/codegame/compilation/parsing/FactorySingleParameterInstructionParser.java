package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

import java.util.Optional;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Parses a named {@link IInstruction instruction} with a single parameter by requesting it
 * from a backing {@link IStringInstructionFactory factory}.
 * The backing {@link IStringInstructionFactory factory} cannot be modified after construction.
 *
 * @author Erik Zemčík
 * @see IStringInstructionFactory
 */
public class FactorySingleParameterInstructionParser extends AbstractNamedParametrizedInstructionParser {

    private final IStringInstructionFactory instructionFactory;

    /**
     * Creates an instance of {@link FactorySingleParameterInstructionParser} that parses
     * {@link IInstruction instructions} with the given name by requesting them from the given backing
     * {@link IStringInstructionFactory factory}.
     *
     * @param instructionName name of the raw {@link IInstruction instruction}
     * @param instructionFactory source of parsed {@link IInstruction instructions}
     *
     * @throws IllegalArgumentException if {@code instructionName} is {@code null} or empty or
     *                                  if {@code instructionFactory} is {@code null}
     */
    public FactorySingleParameterInstructionParser(
        String instructionName,
        IStringInstructionFactory instructionFactory
    ) {
        super(instructionName);
        this.instructionFactory = checkArgumentNotNull(instructionFactory, "Instruction factory");
    }

    /**
     * Parses the named {@link IInstruction instruction} by requesting it from
     * the backing {@link IStringInstructionFactory factory}.
     * The {@code parameters} argument contain a single parameter.
     *
     * @param parameters raw instruction parameters
     * @return {@link IInstruction instruction} from the backing {@link IStringInstructionFactory factory}
     *
     * @throws IllegalArgumentException if {@code parameters} are {@code null}
     *
     * @throws InstructionParseException if length of {@code parameters} is not zero or
     *                                   if {@link IStringInstructionFactory factory} fails to parse
     *                                   the {@link IInstruction instruction}
     */
    @Override
    protected Optional<IInstruction> parseInstructionFromParameters(String[] parameters) {
        this.checkParameterFixedCount(parameters, 1);
        return Optional.of(this.instructionFactory.createInstruction(parameters[0]));
    }

    /**
     * Produces {@link IInstruction instructions} from a single {@link String string} parameter.
     * @author Erik Zemčík
     */
    public interface IStringInstructionFactory {

        /**
         * Creates an {@link IInstruction instruction} ready for use.
         *
         * @param parameter raw {@link String string} parameter representing the requested
         *                  {@link IInstruction instruction} state
         *
         * @return {@link IInstruction instruction} associated with the {@code parameter}
         * @throws InstructionParseException if the factory is unable to parse the {@link IInstruction instruction}
         */
        IInstruction createInstruction(String parameter);

    }

}
