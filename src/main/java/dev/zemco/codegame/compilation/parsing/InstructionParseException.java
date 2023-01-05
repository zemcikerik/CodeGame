package dev.zemco.codegame.compilation.parsing;

/**
 * Thrown when parsing of an {@link dev.zemco.codegame.execution.instructions.IInstruction instruction} fails.
 * @author Erik Zemčík
 */
public class InstructionParseException extends RuntimeException {

    /**
     * Creates an instance of {@link InstructionParseException}.
     * @param message detail message containing information about the parsing failure
     */
    public InstructionParseException(String message) {
        super(message);
    }

    /**
     * Creates an instance of {@link InstructionParseException}.
     * @param message detail message containing information about the parsing failure
     * @param cause cause of parsing failure
     */
    public InstructionParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
