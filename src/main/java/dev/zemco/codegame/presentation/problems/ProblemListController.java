package dev.zemco.codegame.presentation.problems;

import dev.zemco.codegame.presentation.INavigator;
import dev.zemco.codegame.problems.Problem;
import dev.zemco.codegame.util.BindingUtils;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class ProblemListController implements Initializable {

    @FXML
    private ListView<Problem> problemListView;

    @FXML
    private VBox detailBox;

    @FXML
    private Label problemNameLabel;

    @FXML
    private Label problemDescriptionLabel;

    private final IProblemListModel model;
    private final INavigator navigator;

    public ProblemListController(IProblemListModel model, INavigator navigator) {
        this.model = checkArgumentNotNull(model, "Model");
        this.navigator = checkArgumentNotNull(navigator, "Navigator");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.problemListView.itemsProperty().bind(this.model.problemsProperty());

        // TODO: weak
        this.problemListView.getSelectionModel().selectedItemProperty().addListener(this::onProblemListViewItemSelected);

        this.detailBox.visibleProperty().bind(
                Bindings.isNotNull(this.model.selectedProblemProperty())
        );

        this.problemNameLabel.textProperty().bind(
                BindingUtils.mapOrNull(this.model.selectedProblemProperty(), Problem::getName)
        );

        this.problemDescriptionLabel.textProperty().bind(
                BindingUtils.mapOrNull(this.model.selectedProblemProperty(), Problem::getDescription)
        );
    }

    private void onProblemListViewItemSelected(Object ignored, Problem oldValue, Problem newValue) {
        this.model.selectProblem(newValue);
    }

    @FXML
    private void onPlayButtonClicked() {
        this.model.play();
        this.navigator.navigateTo("solution");
    }

}
