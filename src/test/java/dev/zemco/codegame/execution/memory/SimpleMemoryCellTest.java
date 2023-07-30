package dev.zemco.codegame.execution.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag(UNIT_TEST)
class SimpleMemoryCellTest {

    private SimpleMemoryCell memoryCell;

    @BeforeEach
    public void setUp() {
        this.memoryCell = new SimpleMemoryCell();
    }

    @Test
    void shouldHaveNoValueAfterCreation() {
        assertThat(this.memoryCell.hasValue(), is(false));
    }

    @Test
    void hasValueShouldReturnTrueAfterValueWasSet() {
        this.memoryCell.setValue(0);
        assertThat(this.memoryCell.hasValue(), is(true));
    }

    @Test
    void getValueShouldThrowIllegalStateExceptionIfHasNoValue() {
        assertThrows(IllegalStateException.class, () -> this.memoryCell.getValue());
    }

    @Test
    void getValueShouldReturnLastSetValue() {
        this.memoryCell.setValue(5);
        this.memoryCell.setValue(10);
        assertThat(this.memoryCell.getValue(), is(10));
    }

}
