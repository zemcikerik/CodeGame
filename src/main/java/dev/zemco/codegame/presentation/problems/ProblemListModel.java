package dev.zemco.codegame.presentation.problems;

import dev.zemco.codegame.problems.IProblemRepository;
import dev.zemco.codegame.problems.Problem;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Implementation of {@link IProblemListModel problem list model} using {@link Problem problems}
 * managed by {@link IProblemRepository problem repository}.
 *
 * @author Erik Zemčík
 * @see IProblemListModel
 */
public class ProblemListModel implements IProblemListModel {

    private final ReadOnlyObjectWrapper<ObservableList<Problem>> problemsProperty;
    private final ReadOnlyObjectWrapper<Problem> selectedProblemProperty;

    /**
     * Creates an instance of {@link ProblemListModel} using {@link Problem problems} from the given
     * {@link IProblemRepository problem repository}.
     *
     * @param problemRepository problem repository to use as the source of {@link Problem problems}
     * @throws IllegalArgumentException if {@code problemRepository} is {@code null}
     *
     * @see IProblemRepository#getAllProblems()
     */
    public ProblemListModel(IProblemRepository problemRepository) {
        checkArgumentNotNull(problemRepository, "Problem repository");

        List<Problem> problems = problemRepository.getAllProblems();
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
