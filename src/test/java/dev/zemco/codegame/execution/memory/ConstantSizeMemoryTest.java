package dev.zemco.codegame.execution.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConstantSizeMemoryTest {

    private IMemoryCellFactory memoryCellFactory;

    @BeforeEach
    public void setUp() {
        this.memoryCellFactory = mock(IMemoryCellFactory.class);
        when(this.memoryCellFactory.createMemoryCell()).thenReturn(mock(IMemoryCell.class));
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfSizeIsNegative() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ConstantSizeMemory(-5, this.memoryCellFactory)
        );
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfMemoryCellFactoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ConstantSizeMemory(42, null));
    }

    @Test
    public void constructorShouldCreateMemoryCellsUsingMemoryCellFactory() {
        new ConstantSizeMemory(5, this.memoryCellFactory);
        verify(this.memoryCellFactory, times(5)).createMemoryCell();
    }

    @Test
    public void getCellByAddressShouldReturnNonNullMemoryCell() {
        ConstantSizeMemory memory = new ConstantSizeMemory(2, this.memoryCellFactory);
        assertThat(memory.getCellByAddress(1), notNullValue());
    }

    @Test
    public void getCellByAddressShouldReturnWorkingCellIfAddressIsZero() {
        ConstantSizeMemory memory = new ConstantSizeMemory(1, this.memoryCellFactory);
        assertThat(memory.getWorkingCell(), is(memory.getCellByAddress(0)));
    }

    @Test
    public void getCellByAddressShouldThrowIndexOutOfBoundsExceptionIfAddressIsNegative() {
        ConstantSizeMemory memory = new ConstantSizeMemory(2, this.memoryCellFactory);
        assertThrows(IndexOutOfBoundsException.class, () -> memory.getCellByAddress(-1));
    }

    @Test
    public void getCellByAddressShouldThrowIndexOutOfBoundsExceptionIfAddressIsOutOfMemoryRange() {
        ConstantSizeMemory memory = new ConstantSizeMemory(2, this.memoryCellFactory);
        assertThrows(IndexOutOfBoundsException.class, () -> memory.getCellByAddress(5));
    }

    @Test
    public void getWorkingCellShouldReturnNonNullMemoryCell() {
        ConstantSizeMemory memory = new ConstantSizeMemory(1, this.memoryCellFactory);
        assertThat(memory.getWorkingCell(), notNullValue());
    }

}
