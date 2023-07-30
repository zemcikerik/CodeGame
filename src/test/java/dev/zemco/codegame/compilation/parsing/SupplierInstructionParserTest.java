package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.Supplier;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
class SupplierInstructionParserTest {

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
    void constructorShouldThrowIllegalArgumentExceptionIfInstructionNameIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new SupplierInstructionParser(null, this.instructionSupplier)
        );
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionIfInstructionNameIsEmpty() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new SupplierInstructionParser("", this.instructionSupplier)
        );
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionIfSupplierIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new SupplierInstructionParser("test", null));
    }

    @Test
    void parseInstructionShouldThrowIllegalArgumentExceptionIfRawInstructionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.parser.parseInstruction(null));
    }

    @Test
    void parseInstructionShouldThrowIllegalArgumentExceptionIfRawInstructionIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> this.parser.parseInstruction(""));
    }

    @Test
    void parseInstructionShouldReturnEmptyOptionalIfRawInstructionNameDoesNotMatchSupportedInstruction() {
        Optional<IInstruction> result = this.parser.parseInstruction("unknown");
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    void parseInstructionShouldReturnOptionalWithInstructionFromSupplierIfInstructionNameMatches() {
        Optional<IInstruction> result = this.parser.parseInstruction("test");
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(this.instruction));
    }

    @Test
    void parseInstructionShouldIgnoreLeadingAndTrailingWhitespace() {
        Optional<IInstruction> result = this.parser.parseInstruction("   test \t");
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(this.instruction));
    }

    @Test
    void parseInstructionShouldThrowInstructionParsingExceptionIfRawInstructionContainsParameters() {
        assertThrows(InstructionParseException.class, () -> this.parser.parseInstruction("test 2"));
    }

}
