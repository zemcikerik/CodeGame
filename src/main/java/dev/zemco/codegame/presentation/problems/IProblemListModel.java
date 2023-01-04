package dev.zemco.codegame.presentation.problems;

import dev.zemco.codegame.problems.Problem;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;

public interface IProblemListModel {
    void selectProblem(Problem problem);
    void solveSelectedProblem();

    ObservableObjectValue<ObservableList<Problem>> problemsProperty();
    ObservableObjectValue<Problem> selectedProblemProperty();
}
