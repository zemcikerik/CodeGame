package dev.zemco.codegame.presentation.solution;

import dev.zemco.codegame.presentation.IListenerSubscription;
import dev.zemco.codegame.presentation.INavigator;
import dev.zemco.codegame.presentation.dialog.IDialogService;
import dev.zemco.codegame.presentation.highlighting.IHighlightStyleComputer;
import dev.zemco.codegame.presentation.solution.errors.ISolutionErrorModel;
import dev.zemco.codegame.presentation.execution.MemoryView;
import dev.zemco.codegame.problems.Problem;
import dev.zemco.codegame.util.BindingUtils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.collection.ListModification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static javafx.beans.binding.Bindings.not;

/**
 * Controller for the solution view that manipulates provided {@link ISolutionModel solution model}.
 * The solution view presents the details about the {@link Problem problem} to solve.
 * <p>
 * User solves this problem by writing a source code for a program that satisfies the {@link Problem problem's}
 * conditions. When user wants to test his solution, he can try to compile the program. If compilation fails,
 * information about the failure is presented. When compilation succeeds, test evaluation of the solution
 * becomes available.
 * <p>
 * Once user starts the test evaluation, he is presented with the current state of the execution. He may step through
 * the execution. Once the execution successfully satisfies the given {@link Problem problem}, the execution ends.
 * If the execution however fails, user is presented with detailed information about the failure.
 * <p>
 * When the user is sure about validity of his solution, he may submit the solution for evaluation on all cases.
 * If this final evaluation succeeds, the written source code is considered as a valid solution to a given
 * {@link Problem problem}. If this final evaluation fails, no details about the failure are provided.
 * <p>
 * This controller is intended to be {@link #initialize() initialized} using the JavaFX FXML toolkit, as it requires
 * view nodes to be injected using property injection. Target properties are annotated with the {@link FXML} annotation.
 * <p>
 * Some event handler methods are directly referenced by the solution view, and are intended to be bound
 * by JavaFX. These event handlers are also annotated with the {@link FXML} annotation.
 *
 * @author Erik Zemčík
 * @see ISolutionModel
 */
public class SolutionController {

    private static final Collection<String> DEFAULT_LINE_STYLES = Collections.emptyList();
    private static final Collection<String> NEXT_INSTRUCTION_LINE_STYLES = List.of("next-instruction-line");
    private static final Collection<String> SYNTAX_ERROR_LINE_STYLES = List.of("syntax-error");
    private static final Collection<String> EXECUTION_ERROR_LINE_STYLES = List.of("execution-error");

    @FXML
    private Button backButton;

    @FXML
    private Label problemNameLabel;

    @FXML
    private Label problemDescriptionLabel;

    @FXML
    private CodeArea codeArea;

    @FXML
    private Accordion infoAccordion;

    @FXML
    private TitledPane descriptionPane;

    @FXML
    private TitledPane executionPane;

    @FXML
    private MemoryView memoryView;

    @FXML
    private Button compileButton;

    @FXML
    private Button toggleEvaluation;

    @FXML
    private Button submitButton;

    @FXML
    private Button stepButton;

    private final ISolutionModel model;
    private final INavigator navigator;
    private final IDialogService dialogService;
    private final IHighlightStyleComputer highlightStyleComputer;
    private List<IListenerSubscription> listenerSubscriptions;

    /**
     * Creates an instance of {@link SolutionController}.
     * Instances created using this constructor are not ready for use, as they are required to be initialized
     * by the JavaFX FXML toolkit.
     *
     * @param model solution model to manipulate
     * @param navigator navigator to use for navigation to different views
     * @param dialogService dialog service for presenting modal dialogs about the state of evaluation
     * @param highlightStyleComputer computer of highlight styles for user's solution
     *
     * @throws IllegalArgumentException if any argument is {@code null}
     */
    public SolutionController(
        ISolutionModel model,
        INavigator navigator,
        IDialogService dialogService,
        IHighlightStyleComputer highlightStyleComputer
    ) {
        this.model = checkArgumentNotNull(model, "Model");
        this.navigator = checkArgumentNotNull(navigator, "Navigator");
        this.dialogService = checkArgumentNotNull(dialogService, "Dialog service");
        this.highlightStyleComputer = checkArgumentNotNull(
            highlightStyleComputer, "Highlight style computer"
        );
    }

    /**
     * Invoked by the JavaFX FXML toolkit after property injection and event binding is finished.
     * Initializes advanced bindings that are not available for use through FXML.
     */
    @FXML
    private void initialize() {
        this.listenerSubscriptions = new ArrayList<>();

        this.backButton.disableProperty().bind(this.model.evaluationRunningProperty());

        this.initializeProblemDetailPresentation();
        this.initializeSourceCodeEditorPresentation();
        this.initializeEvaluationControlPresentation();
        this.initializeSolutionErrorPresentation();

        this.model.resetAttempt();
    }

    private void initializeProblemDetailPresentation() {
        this.problemNameLabel.textProperty().bind(
            BindingUtils.map(this.model.problemProperty(), Problem::getName)
        );

        this.problemDescriptionLabel.textProperty().bind(
            BindingUtils.map(this.model.problemProperty(), Problem::getDescription)
        );
    }

    private void initializeSourceCodeEditorPresentation() {
        this.codeArea.disableProperty().bind(this.model.evaluationRunningProperty());
        this.codeArea.setParagraphGraphicFactory(LineNumberFactory.get(this.codeArea));

        // trigger alert, thanks RichTextFX
        // observes changed paragraphs in the code area
        // wildcard in paragraph generic was used to simplify the type, as this observer does not need paragraph styles
        Consumer<ListModification<? extends Paragraph<?, String, Collection<String>>>> observer = modification -> {
            // this observer is fired before the code area finishes its internal update process, so we queue up
            // this event handler to be run by the JavaFX event loop after the internal update process finishes
            Platform.runLater(() -> this.onCodeAreaParagraphChanged(modification));
        };

        var paragraphs = this.codeArea.getParagraphs();
        paragraphs.addModificationObserver(observer);
        this.listenerSubscriptions.add(() -> paragraphs.removeModificationObserver(observer));
    }

    private void initializeEvaluationControlPresentation() {
        this.compileButton.disableProperty().bind(not(this.model.canCompileProperty()));

        this.toggleEvaluation.disableProperty().bind(not(this.model.canEvaluateProperty()));
        this.toggleEvaluation.textProperty().bind(
            Bindings.when(this.model.evaluationRunningProperty())
                .then("Stop")
                .otherwise("Start")
        );

        this.stepButton.disableProperty().bind(not(this.model.canStepProperty()));
        this.submitButton.disableProperty().bind(not(this.model.canSubmitProperty()));

        this.executionPane.disableProperty().bind(not(this.model.evaluationRunningProperty()));
        this.memoryView.itemsProperty().bind(this.model.memoryCellsProperty());

        this.subscribeToProperty(this.model.nextInstructionLinePositionProperty(), this::onNextInstructionLineChanged);
    }

    private void initializeSolutionErrorPresentation() {
        this.subscribeToProperty(this.model.syntaxErrorProperty(), this::onSyntaxErrorChanged);
        this.subscribeToProperty(this.model.executionErrorProperty(), this::onExecutionErrorChanged);
    }

    /**
     * Subscribes to a given property and tracks the subscription.
     *
     * @param property property to listen to
     * @param listener listener of property changes
     * @param <T> property value type
     */
    private <T> void subscribeToProperty(ObservableObjectValue<T> property, ChangeListener<T> listener) {
        property.addListener(listener);
        this.listenerSubscriptions.add(() -> property.removeListener(listener));
    }

    @FXML
    private void onBackButtonClicked() {
        this.navigateToPreviousRoute();
    }

    @FXML
    private void onCodeAreaTextChanged() {
        this.model.resetAttempt();
    }

    @FXML
    private void onCompileButtonClicked() {
        this.model.compileSolution(this.codeArea.getText());
    }

    @FXML
    private void onToggleEvaluationButtonClicked() {
        if (this.model.evaluationRunningProperty().get()) {
            this.model.stopTestEvaluation();
            this.infoAccordion.setExpandedPane(this.descriptionPane);
        } else {
            this.model.startTestEvaluation();
            this.infoAccordion.setExpandedPane(this.executionPane);
        }
    }

    @FXML
    private void onSubmitButtonClicked() {
        if (this.model.submitSolution()) {
            String message = "Congratulations! You solved this problem!";
            this.dialogService.showInformationDialog("Submission Success", message);
            this.navigateToPreviousRoute();
        } else {
            String message = "Evaluation failed for some cases!";
            this.dialogService.showErrorDialog("Evaluation Failure", message);
        }
    }

    @FXML
    private void onStepButtonClicked() {
        this.model.stepTestEvaluation();
    }

    /**
     * Recomputes highlight styles for modified paragraphs (lines) using the given {@link IHighlightStyleComputer}.
     * This method is invoked by the code area when the source code is modified.
     *
     * @param modification information about the modified paragraphs
     */
    private void onCodeAreaParagraphChanged(
        ListModification<? extends Paragraph<?, String, Collection<String>>> modification
    ) {
        // prevent triggering of this observer by changes done by this observer or by deletion of entire lines
        if (modification.getAddedSize() == 0) {
            return;
        }

        // for each modified paragraph (line)
        for (int paragraph = modification.getFrom(); paragraph < modification.getTo(); paragraph++) {
            int paragraphLength = this.codeArea.getParagraphLength(paragraph);
            String modifiedText = this.codeArea.getText(paragraph, 0, paragraph, paragraphLength);

            StyleSpans<Collection<String>> styles = this.highlightStyleComputer.computeHighlightStyles(modifiedText);
            int paragraphStartIndex = this.codeArea.getAbsolutePosition(paragraph, 0);

            this.codeArea.setStyleSpans(paragraphStartIndex, styles);
        }
    }

    private void onSyntaxErrorChanged(Object ignored, ISolutionErrorModel oldError, ISolutionErrorModel newError) {
        this.updateErrorLineStyles(oldError, newError, SYNTAX_ERROR_LINE_STYLES);

        if (newError != null) {
            this.showFormattedErrorDialog("Syntax Error", "a syntax error", newError);
        }
    }

    private void onExecutionErrorChanged(Object ignored, ISolutionErrorModel oldError, ISolutionErrorModel newError) {
        this.updateErrorLineStyles(oldError, newError, EXECUTION_ERROR_LINE_STYLES);

        if (newError != null) {
            this.showFormattedErrorDialog("Execution Error", "an error during execution", newError);
        }
    }

    private void onNextInstructionLineChanged(Object ignored, Integer oldPosition, Integer newPosition) {
        this.updateLineStyles(oldPosition, newPosition, NEXT_INSTRUCTION_LINE_STYLES);
    }

    private void updateErrorLineStyles(
        ISolutionErrorModel oldError,
        ISolutionErrorModel newError,
        Collection<String> newPositionStyles
    ) {
        Integer oldPosition = this.getNullableLinePosition(oldError);
        Integer newPosition = this.getNullableLinePosition(newError);
        this.updateLineStyles(oldPosition, newPosition, newPositionStyles);
    }

    private Integer getNullableLinePosition(ISolutionErrorModel errorModel) {
        return errorModel != null ? errorModel.getLinePosition().orElse(null) : null;
    }

    /**
     * Clears the paragraph styles at the old position when it contains the new position styles,
     * and applies the new position styles to the new position.
     * This can be interpreted as moving the styles to different paragraph.
     *
     * @param oldPosition old position to update
     * @param newPosition new position to update
     * @param newPositionStyles styles for new position
     */
    private void updateLineStyles(Integer oldPosition, Integer newPosition, Collection<String> newPositionStyles) {
        if (oldPosition != null) {
            Collection<String> oldPositionStyles = this.codeArea.getParagraph(oldPosition).getParagraphStyle();

            if (oldPositionStyles.equals(newPositionStyles)) {
                this.codeArea.setParagraphStyle(oldPosition, DEFAULT_LINE_STYLES);
            }
        }

        if (newPosition != null) {
            this.codeArea.setParagraphStyle(newPosition, newPositionStyles);
        }
    }

    private void showFormattedErrorDialog(String title, String errorTag, ISolutionErrorModel errorModel) {
        String lineIndicator = errorModel.getLinePosition()
            .map(linePosition -> String.format(" on line %d", linePosition + 1))
            .orElse("");

        String message = String.format("There was %s%s!%n%n%s", errorTag, lineIndicator, errorModel.getDescription());
        this.dialogService.showErrorDialog(title, message);
    }

    /**
     * Navigates back to the main view while performing the necessary cleanup.
     */
    private void navigateToPreviousRoute() {
        this.navigator.navigateTo("problem-list");
        this.cleanup();
    }

    /**
     * Unsubscribes all current listener subscriptions.
     * This method should be called when the view is no longer needed, as it is not presented to the user.
     */
    private void cleanup() {
        this.listenerSubscriptions.forEach(IListenerSubscription::unsubscribe);
    }

}
