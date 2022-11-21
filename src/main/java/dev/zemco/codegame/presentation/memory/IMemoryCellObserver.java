package dev.zemco.codegame.presentation.memory;

import javafx.beans.value.ObservableObjectValue;

public interface IMemoryCellObserver {
    int getAddress();
    ObservableObjectValue<Integer> getValue();
}
