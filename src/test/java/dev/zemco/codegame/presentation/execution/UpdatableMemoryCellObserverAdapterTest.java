package dev.zemco.codegame.presentation.execution;

import dev.zemco.codegame.execution.memory.IMemoryCell;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdatableMemoryCellObserverAdapterTest {

    @Mock
    private IMemoryCell memoryCell;

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfAddressIsNotPositiveInteger() {
        assertThrows(IllegalArgumentException.class, () -> new UpdatableMemoryCellObserverAdapter(-1, this.memoryCell));
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfMemoryCellIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new UpdatableMemoryCellObserverAdapter(2, null));
    }

    @Test
    public void getAddressShouldReturnMemoryCellAddress() {
        UpdatableMemoryCellObserverAdapter adapter = new UpdatableMemoryCellObserverAdapter(1, this.memoryCell);
        assertThat(adapter.getAddress(), is(1));
    }

    @Test
    public void valuePropertyShouldHoldMemoryCellValue() {
        when(this.memoryCell.hasValue()).thenReturn(true);
        when(this.memoryCell.getValue()).thenReturn(42);
        UpdatableMemoryCellObserverAdapter adapter = new UpdatableMemoryCellObserverAdapter(1, this.memoryCell);

        assertThat(adapter.valueProperty().get(), is(42));
    }

    @Test
    public void valuePropertyShouldHoldNullIfMemoryCellDoesNotHaveValue() {
        when(this.memoryCell.hasValue()).thenReturn(false);
        UpdatableMemoryCellObserverAdapter adapter = new UpdatableMemoryCellObserverAdapter(1, this.memoryCell);

        assertThat(adapter.valueProperty().get(), nullValue());
    }

    @Test
    public void updateValueShouldUpdateValueFromMemoryCell() {
        when(this.memoryCell.hasValue()).thenReturn(true);
        when(this.memoryCell.getValue()).thenReturn(42).thenReturn(-13);
        UpdatableMemoryCellObserverAdapter adapter = new UpdatableMemoryCellObserverAdapter(1, this.memoryCell);

        adapter.updateValue();

        assertThat(adapter.valueProperty().get(), is(-13));
    }

}
