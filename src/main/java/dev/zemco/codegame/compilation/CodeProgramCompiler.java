package dev.zemco.codegame.compilation;

import dev.zemco.codegame.compilation.parsing.IInstructionParser;
import dev.zemco.codegame.compilation.parsing.ParseException;
import dev.zemco.codegame.execution.instructions.IInstruction;
import dev.zemco.codegame.programs.IProgramBuilder;
import dev.zemco.codegame.programs.IProgramBuilderFactory;
import dev.zemco.codegame.programs.Program;

import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class CodeProgramCompiler implements IProgramCompiler {

    private static final String COMMENT_PREFIX = ";";
    private static final String JUMP_LABEL_PREFIX = ">";
    private final List<IInstructionParser> instructionParsers;
    private final IProgramBuilderFactory programBuilderFactory;

    public CodeProgramCompiler(
        List<IInstructionParser> instructionParsers,
        IProgramBuilderFactory programBuilderFactory
    ) {
        this.instructionParsers = checkArgumentNotEmpty(instructionParsers, "Instruction parsers");
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

    // try to parse instruction line using provided instruction parsers
    private IInstruction parseInstruction(String instructionLine, int position) throws InvalidSyntaxException {
        for (IInstructionParser parser : this.instructionParsers) {
            // find first parser capable of parsing this instruction line
            if (!parser.canParseInstruction(instructionLine)) {
                continue;
            }

            try {
                return parser.parseInstruction(instructionLine);
            } catch (ParseException e) {
                throw new InvalidSyntaxException("Exception occurred during instruction parsing!", e, position);
            }
        }
        // no parser capable of parsing this instruction line was found
        throw new InvalidSyntaxException("Unknown instruction!", position);
    }

}
