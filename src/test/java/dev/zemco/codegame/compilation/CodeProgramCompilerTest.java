package dev.zemco.codegame.compilation;

import dev.zemco.codegame.compilation.parsing.IInstructionParser;
import dev.zemco.codegame.compilation.parsing.InstructionParseException;
import dev.zemco.codegame.execution.instructions.IInstruction;
import dev.zemco.codegame.programs.IProgramBuilder;
import dev.zemco.codegame.programs.IProgramBuilderFactory;
import dev.zemco.codegame.programs.Program;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
class CodeProgramCompilerTest {

    @Mock
    private IInstructionParser instructionParser;

    @Mock
    private IProgramBuilderFactory programBuilderFactory;

    @Mock
    private IProgramBuilder programBuilder;

    @Mock
    private Program program;

    @InjectMocks
    private CodeProgramCompiler programCompiler;

    @BeforeEach
    public void setUp() {
        lenient().when(this.programBuilderFactory.createProgramBuilder()).thenReturn(this.programBuilder);
        lenient().when(this.programBuilder.build()).thenReturn(this.program);
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionIfInstructionParserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new CodeProgramCompiler(null, this.programBuilderFactory));
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionIfProgramBuilderFactoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new CodeProgramCompiler(this.instructionParser, null));
    }

    @Test
    void compileProgramShouldThrowIllegalArgumentExceptionIfSourceCodeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.programCompiler.compileProgram(null));
    }

    @Test
    void compileProgramShouldReturnEmptyProgramIfSourceCodeIsBlank() throws InvalidSyntaxException {
        Program result = this.programCompiler.compileProgram("");

        assertThat(result, equalTo(this.program));
        verify(this.programBuilder, never()).addInstruction(any(IInstruction.class), anyInt());
        verify(this.programBuilder, never()).addJumpLabelMapping(anyString(), anyInt());
    }

    @Test
    void compileProgramShouldParseInstructionsUsingInstructionParser() throws InvalidSyntaxException {
        IInstruction instruction = this.mockKnownInstruction("test");
        this.programCompiler.compileProgram("test");
        verify(this.programBuilder, times(1)).addInstruction(instruction, 0);
    }

    @Test
    void compileProgramShouldThrowInvalidSyntaxExceptionIfInstructionParserCannotParseInstruction()  {
        this.mockKnownInstruction("known");
        when(this.instructionParser.parseInstruction("unknown")).thenReturn(Optional.empty());

        String sourceCode = this.createSourceCode(
            "known",
            "unknown"
        );

        this.assertCompilationThrowsInvalidSyntaxExceptionAtLine(sourceCode, 1);
    }

    @Test
    void compileProgramShouldThrowInvalidSyntaxExceptionIfInstructionParserFailsToParseInstruction() {
        when(this.instructionParser.parseInstruction("throws")).thenThrow(InstructionParseException.class);
        this.assertCompilationThrowsInvalidSyntaxExceptionAtLine("throws", 0);
    }

    @Test
    void compileProgramShouldIgnoreWhitespace() throws InvalidSyntaxException {
        this.mockKnownInstruction("test");

        String sourceCode = this.createSourceCode(
            "",
            "   test   ",
            "      \t"
        );

        this.programCompiler.compileProgram(sourceCode);

        verify(this.instructionParser, times(1)).parseInstruction("test");
        verify(this.programBuilder, times(1)).addInstruction(any(IInstruction.class), anyInt());
    }

    @Test
    void compileProgramShouldIgnoreComments() throws InvalidSyntaxException {
        this.mockKnownInstruction("instruction");

        String sourceCode = this.createSourceCode(
            ";>comment",
            "instruction;test",
            ";instruction"
        );

        this.programCompiler.compileProgram(sourceCode);

        verify(this.instructionParser, times(1)).parseInstruction("instruction");
        verify(this.programBuilder, times(1)).addInstruction(any(IInstruction.class), anyInt());
    }

    @Test
    void compileProgramShouldProcessJumpLabels() throws InvalidSyntaxException {
        this.programCompiler.compileProgram(">label");
        verify(this.programBuilder, times(1)).addJumpLabelMapping("label", 0);
    }

    @Test
    void compileProgramShouldThrowInvalidSyntaxExceptionIfJumpLabelHasNoName() {
        this.assertCompilationThrowsInvalidSyntaxExceptionAtLine(">", 0);
    }

    @Test
    void compileProgramShouldThrowInvalidSyntaxExceptionIfJumpLabelContainsWhitespace() {
        this.assertCompilationThrowsInvalidSyntaxExceptionAtLine(">test label", 0);
    }

    @Test
    void compileProgramShouldThrowInvalidSyntaxExceptionIfDuplicateJumpLabelIsDefined() {
        when(this.programBuilder.hasJumpLabelMapping("label")).thenReturn(false);
        when(this.programBuilder.hasJumpLabelMapping("duplicate")).thenReturn(false).thenReturn(true);

        String sourceCode = this.createSourceCode(
            ">label",
            ">duplicate",
            ">duplicate"
        );

        this.assertCompilationThrowsInvalidSyntaxExceptionAtLine(sourceCode, 2);
    }

    private void assertCompilationThrowsInvalidSyntaxExceptionAtLine(String sourceCode, int linePosition) {
        InvalidSyntaxException exception = assertThrows(
            InvalidSyntaxException.class,
            () -> this.programCompiler.compileProgram(sourceCode)
        );
        assertThat(exception.getLinePosition(), is(linePosition));
    }


    private IInstruction mockKnownInstruction(String rawInstruction) {
        IInstruction instruction = mock(IInstruction.class);
        when(this.instructionParser.parseInstruction(rawInstruction)).thenReturn(Optional.of(instruction));
        return instruction;
    }

    private String createSourceCode(String... lines) {
        return String.join(System.lineSeparator(), lines);
    }

}
