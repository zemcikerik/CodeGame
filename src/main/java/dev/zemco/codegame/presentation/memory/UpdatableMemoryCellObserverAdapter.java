package dev.zemco.codegame.presentation.memory;

import dev.zemco.codegame.execution.memory.IMemoryCell;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableObjectValue;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

public class UpdatableMemoryCellObserverAdapter implements IMemoryCellObserver {

    private final int address;
    private final IMemoryCell memoryCell;
    private final ReadOnlyObjectWrapper<Integer> observedValue;

    public UpdatableMemoryCellObserverAdapter(int address, IMemoryCell memoryCell) {
        this.address = checkArgumentPositiveInteger(address, "Address");
        this.memoryCell = checkArgumentNotNull(memoryCell, "Memory cell");
        this.observedValue = new ReadOnlyObjectWrapper<>();
        this.updateValue();
    }

    public void updateValue() {
        this.observedValue.set(this.memoryCell.hasValue() ? this.memoryCell.getValue() : null);
    }

    @Override
    public int getAddress() {
        return this.address;
    }

    @Override
    public ObservableObjectValue<Integer> getValue() {
        return this.observedValue.getReadOnlyProperty();
    }

}
