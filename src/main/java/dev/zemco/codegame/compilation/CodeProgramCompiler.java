package dev.zemco.codegame.compilation;

import dev.zemco.codegame.compilation.parsing.InstructionParser;
import dev.zemco.codegame.execution.instructions.Instruction;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class CodeProgramCompiler implements ProgramCompiler {

    private final List<InstructionParser> instructionParsers;

    public CodeProgramCompiler(List<InstructionParser> instructionParsers) {
        if (instructionParsers == null) {
            throw new IllegalArgumentException("Instruction parsers cannot be null!");
        }
        this.instructionParsers = instructionParsers;
    }

    @Override
    public Program compileProgram(String rawProgram) {
        if (rawProgram == null) {
            throw new IllegalArgumentException("Raw program cannot be null!");
        }

        // TODO: line separator
        List<Instruction> instructions = Arrays.stream(rawProgram.split("\\r*\\n"))
                .map(String::trim)
                .filter(not(String::isBlank))
                .filter(not(this::isLineWithComment))
                .map(this::parseInstruction)
                .collect(Collectors.toList());

        return new Program(instructions);
    }

    private boolean isLineWithComment(String line) {
        return line.startsWith(";");
    }

    private Instruction parseInstruction(String instructionLine) {
        return this.instructionParsers.stream()
                .filter(parser -> parser.canParseInstruction(instructionLine))
                .findFirst()
                .map(parser -> parser.parseInstruction(instructionLine))
                .orElseThrow(); // TODO: throw proper exception
    }

}
