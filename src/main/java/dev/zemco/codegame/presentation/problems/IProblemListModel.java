package dev.zemco.codegame.presentation.problems;

import dev.zemco.codegame.problems.Problem;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;

/**
 * Manages logic and state related to the selection of {@link Problem problem} to be solved by the user.
 * A single {@link Problem problem} can be selected for solving via the {@link #selectProblem(Problem)} method.
 * All solvable {@link Problem problems} are available via {@link #problemsProperty()}.
 * Currently selected {@link Problem problem} is available via {@link #selectedProblemProperty()}.
 *
 * @author Erik Zemčík
 */
public interface IProblemListModel {

    /**
     * Selects a {@link Problem problem} for solving. This method accepts {@code null} via the {@code problem} parameter
     * to indicate that no {@link Problem problem} should be selected. All listeners should be notified
     * of a change via the {@link #selectedProblemProperty()}.
     *
     * @param problem problem to select, may be {@code null}
     */
    void selectProblem(Problem problem);

    /**
     * Property holding all {@link Problem problems} solvable by the user.
     * @return read-only property holding {@link Problem problems}
     */
    ObservableObjectValue<ObservableList<Problem>> problemsProperty();

    /**
     * Property holding the currently selected {@link Problem problem} for solving.
     * This property will emit the selected {@link Problem problem} if the selection is changed.
     * This property may hold {@code null} when no {@link Problem problem} is currently selected.
     *
     * @return read-only property holding the selected {@link Problem problem}
     */
    ObservableObjectValue<Problem> selectedProblemProperty();

}
