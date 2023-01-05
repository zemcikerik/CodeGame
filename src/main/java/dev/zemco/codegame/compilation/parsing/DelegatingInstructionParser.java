package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

import java.util.List;
import java.util.Optional;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Implementation of an {@link IInstructionParser instruction parser} that delegates parsing to other provided
 * parsers. The first successfully parsed {@link IInstruction instruction} is used as an output.
 * The used parsers cannot be changed after construction.
 *
 * @author Erik Zemčík
 */
public class DelegatingInstructionParser implements IInstructionParser {

    private final List<IInstructionParser> instructionParsers;

    /**
     * Creates an instance of {@link DelegatingInstructionParser} that delegates {@link IInstruction instruction}
     * parsing to given parsers.
     *
     * @param instructionParsers instruction parsers to use for parsing
     * @throws IllegalArgumentException if {@code instructionParsers} is {@code null}
     */
    public DelegatingInstructionParser(List<IInstructionParser> instructionParsers) {
        checkArgumentNotNull(instructionParsers, "Instruction parsers");
        this.instructionParsers = List.copyOf(instructionParsers);
    }

    /**
     * Delegates parsing of a single {@link IInstruction instruction} encoded in a given {@link String string}
     * to given parsers. This method may return an empty {@link Optional optional}, if no parsers support
     * the given raw {@link IInstruction instruction}.
     *
     * @param rawInstruction {@link IInstruction instruction} encoded in a {@link String string}
     * @return parsed {@link IInstruction instruction} or an empty {@link Optional optional}
     *
     * @throws IllegalArgumentException if {@code rawInstruction} is {@code null} or empty
     * @throws InstructionParseException if any provided parser fails to decode {@code rawInstruction}
     */
    @Override
    public Optional<IInstruction> parseInstruction(String rawInstruction) {
        checkArgumentNotEmpty(rawInstruction, "Raw instruction");

        // return result of the first parser that parses the raw instruction successfully
        for (IInstructionParser parser : this.instructionParsers) {
            Optional<IInstruction> resultInstruction = parser.parseInstruction(rawInstruction);

            if (resultInstruction.isPresent()) {
                return resultInstruction;
            }
        }

        // no parser capable of parsing this instruction line was found
        return Optional.empty();
    }

}
