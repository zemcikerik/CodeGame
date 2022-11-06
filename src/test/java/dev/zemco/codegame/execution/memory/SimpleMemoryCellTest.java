package dev.zemco.codegame.execution.memory;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SimpleMemoryCellTest {

    private SimpleMemoryCell memoryCell;

    @Before
    public void setup() {
        this.memoryCell = new SimpleMemoryCell();
    }

    @Test
    public void shouldHaveNoValueAfterCreation() {
        assertThat(this.memoryCell.hasValue(), is(false));
    }

    @Test
    public void hasValueShouldReturnTrueAfterValueWasSet() {
        this.memoryCell.setValue(0);
        assertThat(this.memoryCell.hasValue(), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void getValueShouldThrowIllegalStateExceptionIfHasNoValue() {
        this.memoryCell.getValue();
    }

    @Test
    public void getValueShouldReturnLastSetValue() {
        this.memoryCell.setValue(5);
        this.memoryCell.setValue(10);
        assertThat(this.memoryCell.getValue(), is(10));
    }

}
