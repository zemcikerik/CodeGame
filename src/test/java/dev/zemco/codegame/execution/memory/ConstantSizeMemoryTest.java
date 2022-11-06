package dev.zemco.codegame.execution.memory;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ConstantSizeMemoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowIfSizeIsNegative() {
        new ConstantSizeMemory(-5);
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

    @Test(expected = IllegalArgumentException.class)
    public void getCellByAddressShouldThrowIllegalArgumentExceptionIfAddressIsNegative() {
        new ConstantSizeMemory(2).getCellByAddress(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getCellByAddressShouldThrowIllegalArgumentExceptionIfAddressIsOutOfMemoryRange() {
        new ConstantSizeMemory(2).getCellByAddress(5);
    }

    @Test
    public void getWorkingCellShouldReturnNonNullMemoryCell() {
        ConstantSizeMemory memory = new ConstantSizeMemory(1);
        assertThat(memory.getWorkingCell(), notNullValue());
    }

}
