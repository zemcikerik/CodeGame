package dev.zemco.codegame.presentation.execution;

import dev.zemco.codegame.execution.memory.IMemoryCell;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableObjectValue;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

/**
 * Adapts existing {@link IMemoryCell memory cell} by observing its value when manually requested
 * using the {@link #updateValue()} method.
 * Value updates are listenable to through the {@link #valueProperty() value property}.
 *
 * @author Erik Zemčík
 */
public class UpdatableMemoryCellObserverAdapter implements IMemoryCellObserver {

    private final int address;
    private final IMemoryCell memoryCell;
    private final ReadOnlyObjectWrapper<Integer> observedValue;

    /**
     * Creates an instance of {@link UpdatableMemoryCellObserverAdapter} adapting and observing
     * an existing {@link IMemoryCell memory cell}.
     *
     * @param address address of the target memory cell within the execution memory
     * @param memoryCell memory cell to adapt and observe
     *
     * @throws IllegalArgumentException if {@code address} is not a positive integer or
     *                                  if {@code memoryCell} is {@code null}
     */
    public UpdatableMemoryCellObserverAdapter(int address, IMemoryCell memoryCell) {
        this.address = checkArgumentPositiveInteger(address, "Address");
        this.memoryCell = checkArgumentNotNull(memoryCell, "Memory cell");
        this.observedValue = new ReadOnlyObjectWrapper<>();

        // retrieve the initial value
        this.updateValue();
    }

    /**
     * Observes the current value of the adapted {@link IMemoryCell memory cell} and notifies its listeners
     * via the {@link #valueProperty()} if the value has changed.
     */
    public void updateValue() {
        Integer value = this.memoryCell.hasValue() ? this.memoryCell.getValue() : null;
        this.observedValue.set(value);
    }

    @Override
    public int getAddress() {
        return this.address;
    }

    @Override
    public ObservableObjectValue<Integer> valueProperty() {
        return this.observedValue.getReadOnlyProperty();
    }

}
