package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class SupplierInstructionParserTest {

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfInstructionNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new SupplierInstructionParser(null, () -> null));
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfInstructionNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new SupplierInstructionParser("", () -> null));
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfInstructionSupplerIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new SupplierInstructionParser("test", null));
    }

    @Test
    public void canParseInstructionShouldReturnTrueIfInstructionIsEqualToInstructionName() {
        SupplierInstructionParser parser = new SupplierInstructionParser("test",  () -> null);
        assertThat(parser.canParseInstruction("test"), is(true));
    }

    @Test
    public void canParseInstructionShouldReturnFalseIfInstructionIsNotEqualToInstructionName() {
        SupplierInstructionParser parser = new SupplierInstructionParser("test", () -> null);
        assertThat(parser.canParseInstruction("unknown"), is(false));
    }

    @Test
    public void parseInstructionShouldReturnInstructionFromInstructionSupplier() {
        IInstruction instruction = mock(IInstruction.class);
        Supplier<IInstruction> instructionSupplier = () -> instruction;

        SupplierInstructionParser parser = new SupplierInstructionParser("test", instructionSupplier);
        assertThat(parser.parseInstruction("test"), sameInstance(instruction));
    }

}
