package dev.zemco.codegame.presentation.problems;

import dev.zemco.codegame.presentation.solution.ISolutionModel;
import dev.zemco.codegame.problems.IProblemRepository;
import dev.zemco.codegame.problems.Problem;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class ProblemListModel implements IProblemListModel {

    private final ISolutionModel solutionModel;
    private final ReadOnlyObjectWrapper<ObservableList<Problem>> problemsProperty;
    private final ReadOnlyObjectWrapper<Problem> selectedProblemProperty;

    public ProblemListModel(ISolutionModel solutionModel, IProblemRepository problemRepository) {
        this.solutionModel = checkArgumentNotNull(solutionModel, "Solution model");
        checkArgumentNotNull(problemRepository, "Problem repository");

        // TODO: loading problem once in a constructor? - maybe move this
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
    public void play() {
        Problem problem = this.selectedProblemProperty.get();

        if (problem == null) {
            throw new IllegalStateException("No problem is currently selected!");
        }

        this.solutionModel.setProblem(problem);
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
