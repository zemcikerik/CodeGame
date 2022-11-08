package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.Instruction;
import org.junit.Test;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SupplierInstructionParserTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowIllegalArgumentExceptionIfInstructionNameIsNull() {
        new SupplierInstructionParser(null, () -> null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowIllegalArgumentExceptionIfInstructionNameIsEmpty() {
        new SupplierInstructionParser("", () -> null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowIllegalArgumentExceptionIfInstructionSupplerIsNull() {
        new SupplierInstructionParser("test", null);
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
        // TODO: warning?
        @SuppressWarnings("unchecked")
        Supplier<Instruction> instructionSupplier = mock(Supplier.class);
        Instruction instruction = mock(Instruction.class);
        when(instructionSupplier.get()).thenReturn(instruction);

        SupplierInstructionParser parser = new SupplierInstructionParser("test", instructionSupplier);
        assertThat(parser.parseInstruction("test"), sameInstance(instruction));
    }

}
