package dev.zemco.codegame.presentation.problems;

import dev.zemco.codegame.problems.Problem;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;

public interface IProblemListModel {
    void play();
    void selectProblem(Problem problem);

    ObservableObjectValue<ObservableList<Problem>> problemsProperty();
    ObservableObjectValue<Problem> selectedProblemProperty();
}
