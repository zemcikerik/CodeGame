package dev.zemco.codegame.compilation;

import dev.zemco.codegame.compilation.parsing.IInstructionParser;
import dev.zemco.codegame.compilation.parsing.InstructionParseException;
import dev.zemco.codegame.execution.instructions.IInstruction;
import dev.zemco.codegame.programs.IProgramBuilder;
import dev.zemco.codegame.programs.IProgramBuilderFactory;
import dev.zemco.codegame.programs.Program;

import java.util.Optional;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Implementation of a compiler, which compiles source code into an executable {@link Program program} using
 * the CodeGame syntax. Instructions are parsed using provided {@link IInstructionParser instruction parser}
 * and the final {@link Program program} is built using provided {@link IProgramBuilderFactory program builder factory}.
 * <p>
 * CodeGame syntax includes following features:
 * <ul>
 *     <li>every {@link IInstruction instruction} is exactly one line long</li>
 *     <li>every jump label is exactly one line long</li>
 *     <li>jump labels are prefixed using the {@link #JUMP_LABEL_PREFIX jump label prefix} and can contain
 *         any non-whitespace character</li>
 *     <li>comments are prefixed using the {@link #COMMENT_PREFIX comment prefix} and end at the end of the line</li>
 *     <li>comments are completely ignored during compilation and may be placed
 *         behind {@link IInstruction instructions} or jump labels</li>
 *     <li>black lines / lines containing only whitespace/comments are completely ignored</li>
 *     <li>duplicate jump labels are not allowed</li>
 * </ul>
 *
 * @author Erik Zemčík
 */
public class CodeProgramCompiler implements IProgramCompiler {

    public static final String COMMENT_PREFIX = ";";
    public static final String JUMP_LABEL_PREFIX = ">";

    private final IInstructionParser instructionParser;
    private final IProgramBuilderFactory programBuilderFactory;

    /**
     * Creates an instance of {@link CodeProgramCompiler}.
     *
     * @param instructionParser instruction parser for parsing instruction
     * @param programBuilderFactory factory for building output {@link Program program}
     *
     * @throws IllegalArgumentException if {@code instructionParser} is {@code null} or
     *                                  if {@code programBuilderFactory} is {@code null}
     */
    public CodeProgramCompiler(IInstructionParser instructionParser, IProgramBuilderFactory programBuilderFactory) {
        this.instructionParser = checkArgumentNotNull(instructionParser, "Instruction parser");
        this.programBuilderFactory = checkArgumentNotNull(programBuilderFactory, "Program builder factory");
    }

    @Override
    public Program compileProgram(String sourceCode) throws InvalidSyntaxException {
        checkArgumentNotNull(sourceCode, "Source code");

        IProgramBuilder programBuilder = this.programBuilderFactory.createProgramBuilder();

        // first we split the program into individual lines
        // \R since Java 8 matches any unicode line break sequence
        String[] lines = sourceCode.split("\\R");

        for (int position = 0; position < lines.length; position++) {
            String lineWithoutComment = this.removeCommentFromLine(lines[position]);

            // remove leading and trailing whitespace used by user for formatting
            String instructionLine = lineWithoutComment.trim();

            if (instructionLine.isEmpty()) {
                // line was used by user for readability / comments, thus it does not contain any instruction or label
                continue;
            }

            // if line is a jump label
            if (instructionLine.startsWith(JUMP_LABEL_PREFIX)) {
                if (this.containsAnyWhitespace(instructionLine)) {
                    throw new InvalidSyntaxException("Whitespace is disallowed in jump labels!", position);
                }

                if (instructionLine.length() == JUMP_LABEL_PREFIX.length()) {
                    throw new InvalidSyntaxException("Jump label name cannot be empty!", position);
                }

                String label = instructionLine.substring(JUMP_LABEL_PREFIX.length());

                if (programBuilder.hasJumpLabelMapping(label)) {
                    throw new InvalidSyntaxException("Duplicate jump label name is not allowed!", position);
                }

                programBuilder.addJumpLabelMapping(label, position);
                continue;
            }

            IInstruction instruction = this.parseInstruction(instructionLine, position);
            programBuilder.addInstruction(instruction, position);
        }

        return programBuilder.build();
    }

    // removes all characters after comment prefix (including the comment prefix)
    private String removeCommentFromLine(String line) {
        int commentStartIndex = line.indexOf(COMMENT_PREFIX);
        return commentStartIndex != -1 ? line.substring(0, commentStartIndex) : line;
    }

    private boolean containsAnyWhitespace(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (Character.isWhitespace(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private IInstruction parseInstruction(String instructionLine, int position) throws InvalidSyntaxException {
        try {
            Optional<IInstruction> instruction = this.instructionParser.parseInstruction(instructionLine);

            if (instruction.isPresent()) {
                return instruction.get();
            }
        } catch (InstructionParseException e) {
            throw new InvalidSyntaxException("Exception occurred during instruction parsing!", e, position);
        }

        // parser was not capable of parsing this instruction line
        throw new InvalidSyntaxException("Unknown instruction!", position);
    }

}
