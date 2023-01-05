package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

import java.util.Optional;

/**
 * Parses {@link IInstruction instructions} encoded in {@link String strings}.
 * The encoded {@link IInstruction instruction} is typically a single line of source code.
 * A single instruction parser may not support all types of raw {@link IInstruction instructions}.
 *
 * @author Erik Zemčík
 * @see IInstruction
 */
public interface IInstructionParser {

    /**
     * Parses a single {@link IInstruction instruction} encoded in a given {@link String string}.
     * This method may return an empty {@link Optional optional}, if the parser does not support
     * the given raw {@link IInstruction instruction}.
     *
     * @param rawInstruction {@link IInstruction instruction} encoded in a {@link String string}
     * @return parsed {@link IInstruction instruction} or an empty {@link Optional optional}
     *
     * @throws IllegalArgumentException if {@code rawInstruction} is {@code null} or empty
     * @throws InstructionParseException if {@code rawInstruction} is not encoded correctly
     */
    Optional<IInstruction> parseInstruction(String rawInstruction);

}
