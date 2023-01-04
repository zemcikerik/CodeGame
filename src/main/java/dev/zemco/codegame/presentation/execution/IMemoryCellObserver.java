package dev.zemco.codegame.presentation.execution;

import dev.zemco.codegame.execution.memory.IMemoryCell;
import javafx.beans.value.ObservableObjectValue;

/**
 * Observes value changes of a {@link IMemoryCell memory cell} and notifies listeners about new values.
 * Value updates are listenable to through the {@link #valueProperty() value property}.
 *
 * @author Erik Zemčík
 * @see IMemoryCell
 */
public interface IMemoryCellObserver {

    /**
     * Returns the address of the target {@link IMemoryCell memory cell} within the execution
     * {@link dev.zemco.codegame.execution.memory.IMemory memory}.
     *
     * @return address of the {@link IMemoryCell memory cell}
     */
    int getAddress();

    /**
     * Property indicating last value of the observed {@link IMemoryCell memory cell}.
     * This property will emit the observed value once the observer observes a change.
     * This property may hold {@code null} when the observed {@link IMemoryCell memory cell} holds no value.
     *
     * @return read-only property indicating last observed value
     */
    ObservableObjectValue<Integer> valueProperty();

}
