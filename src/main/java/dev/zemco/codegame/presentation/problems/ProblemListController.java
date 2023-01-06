package dev.zemco.codegame.presentation.problems;

import dev.zemco.codegame.presentation.IListenerSubscription;
import dev.zemco.codegame.presentation.INavigator;
import dev.zemco.codegame.problems.Problem;
import dev.zemco.codegame.util.BindingUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.layout.VBox;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Controller for the problem list view that manipulates provided {@link IProblemListModel problem list model}.
 * The problem list view presents a list of solvable {@link Problem problems} to the user.
 * User can choose a single problem to display its details and can then attempt to solve the problem.
 * <p>
 * This controller is intended to be {@link #initialize() initialized} using the JavaFX FXML toolkit, as it requires
 * view nodes to be injected using property injection. Target properties are annotated with the {@link FXML} annotation.
 * <p>
 * Some event handler methods are directly referenced by the problem list view, and are intended to be bound
 * by JavaFX. These event handlers are also annotated with the {@link FXML} annotation.
 *
 * @author Erik Zemčík
 * @see IProblemListModel
 */
public class ProblemListController {

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
    private IListenerSubscription problemSelectionSubscription;

    /**
     * Creates an instance of {@link ProblemListController}.
     * Instances created using this constructor are not ready for use, as they are required to be initialized
     * by the JavaFX FXML toolkit.
     *
     * @param model problem list model to manipulate
     * @param navigator navigator to use for navigation to different views
     *
     * @throws IllegalArgumentException if {@code model} is {@code null} or if {@code navigator} is {@code null}
     */
    public ProblemListController(IProblemListModel model, INavigator navigator) {
        this.model = checkArgumentNotNull(model, "Model");
        this.navigator = checkArgumentNotNull(navigator, "Navigator");
    }

    /**
     * Invoked by the JavaFX FXML toolkit after property injection and event binding is finished.
     * Initializes advanced bindings that are not available for use through FXML.
     */
    @FXML
    private void initialize() {
        this.problemListView.itemsProperty().bind(this.model.problemsProperty());
        this.initializeProblemSelectionListener();
        this.preselectCurrentProblemInListView();

        // show problem details only when a problem is selected
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

    /**
     * Initializes the listener for problem selection by the user.
     * This subscription is stored in the {@link #problemSelectionSubscription} field for later cleanup.
     */
    private void initializeProblemSelectionListener() {
        MultipleSelectionModel<Problem> selectionModel = this.problemListView.getSelectionModel();
        ReadOnlyObjectProperty<Problem> selectedItemProperty = selectionModel.selectedItemProperty();

        // subscribe to the property and provide a simple way to remove the listener
        ChangeListener<Problem> selectionListener = this::onProblemListViewItemSelection;
        selectedItemProperty.addListener(selectionListener);
        this.problemSelectionSubscription = () -> selectedItemProperty.removeListener(selectionListener);
    }

    /**
     * Preselects the currently selected problem by the {@link IProblemListModel problem list model} in
     * the {@link #problemListView problem list view}. This is useful during initialization, when model can already
     * have a problem selected, and the list view needs to be updated.
     */
    private void preselectCurrentProblemInListView() {
        Problem problem = this.model.selectedProblemProperty().get();

        if (problem != null) {
            this.problemListView.getSelectionModel().select(problem);
            this.problemListView.scrollTo(problem);
        }
    }

    /**
     * Passes the new selected problem to the {@link IProblemListModel problem list model}.
     * This method is invoked by the problem list when a selected problem is changed.
     *
     * @param observable ignored, required by the {@link ChangeListener change listener} interface
     * @param oldProblem ignored, required by the {@link ChangeListener change listener} interface
     * @param newProblem new problem selected by the user
     */
    private void onProblemListViewItemSelection(Object observable, Problem oldProblem, Problem newProblem) {
        this.model.selectProblem(newProblem);
    }

    /**
     * Navigates to the view responsible for solution of the currently selected problem.
     * This method is invoked when the play button is clicked.
     *
     * @throws IllegalStateException if no problem is selected
     */
    @FXML
    private void onPlayButtonClicked() {
        if (this.model.selectedProblemProperty().get() == null) {
            throw new IllegalStateException("No problem is currently selected!");
        }

        this.navigator.navigateTo("solution");
        this.cleanup();
    }

    /**
     * Unsubscribes all current listener subscriptions.
     * This method should be called when the view is no longer needed, as it is not presented to the user.
     */
    private void cleanup() {
        this.problemSelectionSubscription.unsubscribe();
    }

}
