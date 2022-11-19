package dev.zemco.codegame.presentation.solution;

import dev.zemco.codegame.presentation.errors.IProgramErrorModel;
import dev.zemco.codegame.problems.Problem;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;

public interface ISolutionModel {
    void setProblem(Problem problem);
    void submitAttemptForCompilation(String program);
    void startExecution();
    void stopExecution();
    void resetAttempt();

    ObservableObjectValue<Problem> problemProperty();
    ObservableBooleanValue canCompileProperty();
    ObservableBooleanValue canExecuteProperty();
    ObservableBooleanValue canStepProperty();
    ObservableBooleanValue executionRunningProperty();
    ObservableObjectValue<IProgramErrorModel> syntaxErrorProperty();
}
