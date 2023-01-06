package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

import java.util.Arrays;
import java.util.Optional;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

/**
 * Abstract base for {@link IInstructionParser instruction parsers} parsing a single named
 * {@link IInstruction instruction} encoded with parameters separated by whitespace.
 * First part of a raw {@link IInstruction instruction} is treated as the instruction name.
 *
 * @author Erik Zemčík
 */
public abstract class AbstractNamedParametrizedInstructionParser implements IInstructionParser {

    private final String instructionName;

    /**
     * Initializes a subclass instance of {@link AbstractNamedParametrizedInstructionParser} that parses
     * a parametrized {@link IInstruction instruction} with the given name.
     *
     * @param instructionName name of the {@link IInstruction instruction} to parse
     * @throws IllegalArgumentException if {@code instructionName} is {@code null}
     */
    protected AbstractNamedParametrizedInstructionParser(String instructionName) {
        this.instructionName = checkArgumentNotEmpty(instructionName, "Instruction name");
    }

    /**
     * Parses the named {@link IInstruction instruction} from its given parameters.
     * This method may return an empty {@link Optional optional}, if the parser does not support
     * the given raw {@link IInstruction instruction}.
     *
     * @param parameters raw instruction parameters
     * @return parsed {@link IInstruction instruction} or an empty {@link Optional optional}
     */
    protected abstract Optional<IInstruction> parseInstructionFromParameters(String[] parameters);

    @Override
    public Optional<IInstruction> parseInstruction(String rawInstruction) {
        checkArgumentNotEmpty(rawInstruction, "Raw instruction");
        String trimmedInstruction = rawInstruction.trim();

        // split into parameters separated by whitespace of 1 to n length (we treat multiple spaces as one separator)
        String[] instructionParts = trimmedInstruction.split("\\s+");

        if (!this.instructionName.equals(instructionParts[0])) {
            return Optional.empty();
        }

        // take parameters without the instruction name
        String[] parameters = Arrays.copyOfRange(instructionParts, 1, instructionParts.length);
        return this.parseInstructionFromParameters(parameters);
    }

    /**
     * Checks if the given parameters have a required fixed count.
     * This method is intended to be used by subclasses which require a fixed parameter count for their
     * {@link IInstruction instruction} parsing.
     *
     * @param parameters instruction parameters
     * @param parameterCount expected fixed instruction parameter count
     *
     * @throws IllegalArgumentException if {@code parameters} is {@code null} or
     *                                  if {@code parameterCount} is not a positive integer
     * @throws InstructionParseException if the count of parameters does not match the requested {@code parameterCount}
     */
    protected void checkParameterFixedCount(String[] parameters, int parameterCount) {
        checkArgumentNotNull(parameters, "Parameters");
        checkArgumentPositiveInteger(parameterCount, "Parameter count");

        if (parameters.length == parameterCount) {
            return;
        }

        String parameterQuantity = parameterCount == 1 ? "parameter" : "parameters";

        throw new InstructionParseException(String.format(
            "Instruction '%s' expected %d %s, found %d!",
            this.instructionName, parameterCount, parameterQuantity, parameters.length
        ));
    }

}
