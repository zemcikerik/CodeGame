package dev.zemco.codegame.presentation.problems;

import dev.zemco.codegame.problems.IProblemService;
import dev.zemco.codegame.problems.Problem;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Implementation of problem list model using {@link Problem problems}
 * managed by {@link IProblemService problem service}.
 *
 * @author Erik Zemčík
 * @see IProblemListModel
 */
public class ProblemListModel implements IProblemListModel {

    private final ReadOnlyObjectWrapper<ObservableList<Problem>> problemsProperty;
    private final ReadOnlyObjectWrapper<Problem> selectedProblemProperty;

    /**
     * Creates an instance of {@link ProblemListModel} using {@link Problem problems} from the given
     * {@link IProblemService problem service}.
     *
     * @param problemService problem service to use as the source of {@link Problem problems}
     * @throws IllegalArgumentException if {@code problemService} is {@code null}
     */
    public ProblemListModel(IProblemService problemService) {
        checkArgumentNotNull(problemService, "Problem service");

        List<Problem> problems = problemService.getAllProblems();
        ObservableList<Problem> observableProblems = FXCollections.observableList(problems);
        this.problemsProperty = new ReadOnlyObjectWrapper<>(observableProblems);

        this.selectedProblemProperty = new ReadOnlyObjectWrapper<>(null);
    }

    @Override
    public void selectProblem(Problem problem) {
        this.selectedProblemProperty.set(problem);
    }

    @Override
    public ObservableObjectValue<ObservableList<Problem>> problemsProperty() {
        return this.problemsProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableObjectValue<Problem> selectedProblemProperty() {
        return this.selectedProblemProperty.getReadOnlyProperty();
    }

}
