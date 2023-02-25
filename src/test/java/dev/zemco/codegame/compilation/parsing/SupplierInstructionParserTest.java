package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class SupplierInstructionParserTest {

    @Mock
    private Supplier<IInstruction> instructionSupplier;

    @Mock
    private IInstruction instruction;

    private SupplierInstructionParser parser;

    @BeforeEach
    public void setUp() {
        lenient().when(this.instructionSupplier.get()).thenReturn(this.instruction);
        this.parser = new SupplierInstructionParser("test", this.instructionSupplier);
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfInstructionNameIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new SupplierInstructionParser(null, this.instructionSupplier)
        );
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfInstructionNameIsEmpty() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new SupplierInstructionParser("", this.instructionSupplier)
        );
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfSupplierIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new SupplierInstructionParser("test", null));
    }

    @Test
    public void parseInstructionShouldThrowIllegalArgumentExceptionIfRawInstructionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.parser.parseInstruction(null));
    }

    @Test
    public void parseInstructionShouldThrowIllegalArgumentExceptionIfRawInstructionIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> this.parser.parseInstruction(""));
    }

    @Test
    public void parseInstructionShouldReturnEmptyOptionalIfRawInstructionNameDoesNotMatchSupportedInstruction() {
        Optional<IInstruction> result = this.parser.parseInstruction("unknown");
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void parseInstructionShouldReturnOptionalWithInstructionFromSupplierIfInstructionNameMatches() {
        Optional<IInstruction> result = this.parser.parseInstruction("test");
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(this.instruction));
    }

    @Test
    public void parseInstructionShouldIgnoreLeadingAndTrailingWhitespace() {
        Optional<IInstruction> result = this.parser.parseInstruction("   test \t");
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(this.instruction));
    }

    @Test
    public void parseInstructionShouldThrowInstructionParsingExceptionIfRawInstructionContainsParameters() {
        assertThrows(InstructionParseException.class, () -> this.parser.parseInstruction("test 2"));
    }

}
