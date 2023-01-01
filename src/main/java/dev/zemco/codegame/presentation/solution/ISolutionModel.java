package dev.zemco.codegame.presentation.solution;

import dev.zemco.codegame.presentation.errors.IProgramErrorModel;
import dev.zemco.codegame.presentation.memory.IMemoryCellObserver;
import dev.zemco.codegame.problems.Problem;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;

// TODO: this has grown to be pretty big, refactor this into smaller parts if possible
public interface ISolutionModel {
    void setProblem(Problem problem);
    void submitSolution(String program);
    void startExecution();
    void stepExecution();
    void stopExecution();
    void resetAttempt();

    ObservableObjectValue<Problem> problemProperty();
    ObservableBooleanValue canCompileProperty();
    ObservableBooleanValue canExecuteProperty();
    ObservableBooleanValue canStepProperty();
    ObservableBooleanValue executionRunningProperty();

    ObservableObjectValue<Integer> nextInstructionLinePositionProperty();
    ObservableObjectValue<ObservableList<IMemoryCellObserver>> memoryCellsProperty();

    ObservableObjectValue<IProgramErrorModel> syntaxErrorProperty();
    ObservableObjectValue<IProgramErrorModel> executionErrorProperty();
}
