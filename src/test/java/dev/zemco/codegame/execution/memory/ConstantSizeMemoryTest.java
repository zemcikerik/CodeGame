package dev.zemco.codegame.execution.memory;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConstantSizeMemoryTest {

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfSizeIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new ConstantSizeMemory(-5));
    }

    @Test
    public void getCellByAddressShouldReturnNonNullMemoryCell() {
        ConstantSizeMemory memory = new ConstantSizeMemory(2);
        assertThat(memory.getCellByAddress(1), notNullValue());
    }

    @Test
    public void getCellByAddressShouldReturnWorkingCellIfAddressIsZero() {
        ConstantSizeMemory memory = new ConstantSizeMemory(1);
        assertThat(memory.getWorkingCell(), is(memory.getCellByAddress(0)));
    }

    @Test
    public void getCellByAddressShouldThrowIllegalArgumentExceptionIfAddressIsNegative() {
        ConstantSizeMemory memory = new ConstantSizeMemory(2);
        assertThrows(IllegalArgumentException.class, () -> memory.getCellByAddress(-1));
    }

    @Test
    public void getCellByAddressShouldThrowIllegalArgumentExceptionIfAddressIsOutOfMemoryRange() {
        ConstantSizeMemory memory = new ConstantSizeMemory(2);
        assertThrows(IndexOutOfBoundsException.class, () -> memory.getCellByAddress(5));
    }

    @Test
    public void getWorkingCellShouldReturnNonNullMemoryCell() {
        ConstantSizeMemory memory = new ConstantSizeMemory(1);
        assertThat(memory.getWorkingCell(), notNullValue());
    }

}
