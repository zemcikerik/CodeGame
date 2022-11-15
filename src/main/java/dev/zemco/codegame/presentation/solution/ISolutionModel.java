package dev.zemco.codegame.presentation.solution;

import dev.zemco.codegame.problems.Problem;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;

public interface ISolutionModel {
    void submitAttemptForCompilation(String program);
    void startExecution();
    void stopExecution();
    void resetAttempt();
    Problem getProblem();

    ObservableBooleanValue canCompileProperty();
    ObservableBooleanValue canExecuteProperty();
    ObservableBooleanValue canStepProperty();
    ObservableBooleanValue executionRunningProperty();
    ObservableValue<SyntaxErrorModel> syntaxErrorProperty();
}
