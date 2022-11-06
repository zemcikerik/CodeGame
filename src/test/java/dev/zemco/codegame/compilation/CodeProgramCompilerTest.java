package dev.zemco.codegame.compilation;

import dev.zemco.codegame.compilation.parsing.InstructionParser;
import dev.zemco.codegame.execution.instructions.Instruction;
import org.junit.Test;

import java.util.List;

import static java.lang.System.lineSeparator;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CodeProgramCompilerTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowIfInstructionParserListIsNull() {
        new CodeProgramCompiler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compileProgramShouldThrowIllegalArgumentExceptionIfRawProgramIsEmpty() {
        new CodeProgramCompiler(emptyList()).compileProgram(null);
    }

    @Test
    public void compileProgramShouldReturnProgramWithNoInstructionsWhenRawProgramIsEmpty() {
        CodeProgramCompiler compiler = new CodeProgramCompiler(emptyList());
        assertThat(compiler.compileProgram("").getInstructions(), empty());
    }

    @Test
    public void compileProgramShouldUseInstructionParsersForInstructionsInRawProgram() {
        // given
        Instruction fooInstruction = mock(Instruction.class);
        InstructionParser fooParser = this.mockInstructionParser(fooInstruction, "foo");

        Instruction barInstruction = mock(Instruction.class);
        InstructionParser barParser = this.mockInstructionParser(barInstruction, "bar");

        CodeProgramCompiler compiler = new CodeProgramCompiler(of(fooParser, barParser));
        String rawProgram = String.join(lineSeparator(), "foo", "foo", "bar");

        // when
        Program program = compiler.compileProgram(rawProgram);
        List<Instruction> instructions = program.getInstructions();

        // then
        assertThat(instructions, equalTo(of(fooInstruction, fooInstruction, barInstruction)));
        verify(fooParser, times(2)).parseInstruction("foo");
        verify(barParser, times(1)).parseInstruction("bar");
    }

    @Test
    public void compileProgramShouldIgnoreEmptyLinesInRawProgram() {
        InstructionParser testParser = this.mockInstructionParser("test");
        CodeProgramCompiler compiler = new CodeProgramCompiler(of(testParser));
        String rawProgram = String.join(lineSeparator(), "test", "", "", "test", "");

        Program program = compiler.compileProgram(rawProgram);
        assertThat(program.getInstructions(), hasSize(2));
    }

    @Test
    public void compileProgramShouldIgnoreWhitespaceBeforeAndAfterInstructionLineInRawProgram() {
        InstructionParser fooBarParser = this.mockInstructionParser("foo bar");
        CodeProgramCompiler compiler = new CodeProgramCompiler(of(fooBarParser));
        String rawProgram = String.join(lineSeparator(), " foo bar", "foo bar ", "  \tfoo bar\t", "   ");

        Program program = compiler.compileProgram(rawProgram);
        assertThat(program.getInstructions(), hasSize(3));
    }

    // TODO: specify exception
    @Test
    public void compileProgramShouldThrowTODOExceptionIfRawProgramContainsUnknownInstruction() {
        InstructionParser testParser = this.mockInstructionParser("test");
        CodeProgramCompiler compiler = new CodeProgramCompiler(of(testParser));
        String rawProgram = String.join(lineSeparator(), "test", "unknown", "test");

        compiler.compileProgram(rawProgram);
    }

    private InstructionParser mockInstructionParser(String instructionName) {
        return this.mockInstructionParser(mock(Instruction.class), instructionName);
    }

    private InstructionParser mockInstructionParser(Instruction instruction, String instructionName) {
        InstructionParser parser = mock(InstructionParser.class);
        when(parser.canParseInstruction(anyString())).thenReturn(false);
        when(parser.canParseInstruction(instructionName)).thenReturn(true);
        when(parser.parseInstruction(anyString())).thenReturn(instruction);
        return parser;
    }

}
