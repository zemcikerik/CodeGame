package dev.zemco.codegame.presentation.solution;

import dev.zemco.codegame.presentation.INavigator;
import dev.zemco.codegame.presentation.dialog.IDialogService;
import dev.zemco.codegame.presentation.errors.IProgramErrorModel;
import dev.zemco.codegame.presentation.memory.MemoryView;
import dev.zemco.codegame.problems.Problem;
import dev.zemco.codegame.util.BindingUtils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.collection.ListModification;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class SolutionController implements Initializable {

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
    private Button toggleExecutionButton;

    @FXML
    private Button stepButton;

    @FXML
    private CheckBox autoEnabledCheckbox;

    @FXML
    private Slider autoSpeedSlider;

    private final ISolutionModel model;
    private final INavigator navigator;
    private final IDialogService dialogService;
    private final IHighlightStyleComputer highlightStyleComputer;

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

    // TODO: split this up into smaller parts
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.backButton.disableProperty().bind(this.model.executionRunningProperty());

        this.problemNameLabel.textProperty().bind(
            BindingUtils.map(this.model.problemProperty(), Problem::getName)
        );

        this.problemDescriptionLabel.textProperty().bind(
            BindingUtils.map(this.model.problemProperty(), Problem::getDescription)
        );

        this.codeArea.disableProperty().bind(this.model.executionRunningProperty());
        this.codeArea.setParagraphGraphicFactory(LineNumberFactory.get(this.codeArea));

        this.codeArea.getParagraphs().addModificationObserver(modification -> {
            // this observer is fired before the code area finishes its internal update process, so we queue up
            // this event handler to be run by the JavaFX event loop after the internal update process finishes
            Platform.runLater(() -> this.onCodeAreaParagraphChanged(modification));
        });

        this.executionPane.disableProperty().bind(Bindings.not(this.model.executionRunningProperty()));
        this.memoryView.itemsProperty().bind(this.model.memoryCellsProperty());

        this.compileButton.disableProperty().bind(Bindings.not(this.model.canCompileProperty()));

        this.toggleExecutionButton.disableProperty().bind(Bindings.not(this.model.canExecuteProperty()));
        this.toggleExecutionButton.textProperty().bind(
            Bindings.when(this.model.executionRunningProperty())
                .then("Stop")
                .otherwise("Start")
        );

        this.stepButton.disableProperty().bind(Bindings.not(this.model.canStepProperty()));

        // TODO: weak
        this.model.syntaxErrorProperty().addListener(this::onSyntaxErrorChanged);
        this.model.executionErrorProperty().addListener(this::onExecutionErrorChanged);
        this.model.nextInstructionLinePositionProperty().addListener(this::onNextInstructionLineChanged);
    }

    @FXML
    private void onBackButtonClicked() {
        // TODO: remove magic constant
        this.navigator.navigateTo("problem-list");
    }

    @FXML
    private void onCodeAreaTextChanged() {
        this.model.resetAttempt();
    }

    @FXML
    private void onCompileButtonClicked() {
        this.model.submitSolution(this.codeArea.getText());
    }

    @FXML
    private void onToggleExecutionButtonClicked() {
        if (this.model.executionRunningProperty().get()) {
            this.model.stopExecution();
            this.infoAccordion.setExpandedPane(this.descriptionPane);
        } else {
            this.model.startExecution();
            this.infoAccordion.setExpandedPane(this.executionPane);
        }
    }

    @FXML
    private void onStepButtonClicked() {
        this.model.stepExecution();
    }

    private void onCodeAreaParagraphChanged(
        ListModification<? extends Paragraph<Collection<String>, String, Collection<String>>> modification
    ) {
        // prevent triggering of this observer by changes done by this observer or by deletion of entire lines
        if (modification.getAddedSize() == 0) {
            return;
        }

        // for each modified paragraph
        for (int paragraph = modification.getFrom(); paragraph < modification.getTo(); paragraph++) {
            int paragraphLength = this.codeArea.getParagraphLength(paragraph);
            String modifiedText = this.codeArea.getText(paragraph, 0, paragraph, paragraphLength);

            StyleSpans<Collection<String>> styles = this.highlightStyleComputer.computeHighlightStyles(modifiedText);
            int paragraphStartIndex = this.codeArea.getAbsolutePosition(paragraph, 0);

            this.codeArea.setStyleSpans(paragraphStartIndex, styles);
        }
    }

    // TODO: remove duplicate code
    private void onSyntaxErrorChanged(Object ignored, IProgramErrorModel oldError, IProgramErrorModel newError) {
        this.updateErrorLineStyles(oldError, newError, SYNTAX_ERROR_LINE_STYLES);

        if (newError != null) {
            String header = String.format("There was a syntax error on line %d!", newError.getLinePosition() + 1);
            String message = String.format("%s%n%n%s", header, newError.getDescription());
            this.dialogService.showErrorDialog("Syntax Error", message);
        }
    }

    private void onExecutionErrorChanged(Object ignored, IProgramErrorModel oldError, IProgramErrorModel newError) {
        this.updateErrorLineStyles(oldError, newError, EXECUTION_ERROR_LINE_STYLES);

        if (newError != null) {
            String header = String.format("There was an error during execution on line %d!", newError.getLinePosition() + 1);
            String message = String.format("%s%n%n%s", header, newError.getDescription());
            this.dialogService.showErrorDialog("Execution Error", message);
        }
    }

    private void onNextInstructionLineChanged(Object ignored, Integer oldPosition, Integer newPosition) {
        this.updateLineStyles(oldPosition, newPosition, NEXT_INSTRUCTION_LINE_STYLES);
    }

    private void updateErrorLineStyles(
        IProgramErrorModel oldError,
        IProgramErrorModel newError,
        Collection<String> newPositionStyles
    ) {
        Integer oldPosition = oldError != null ? oldError.getLinePosition() : null;
        Integer newPosition = newError != null ? newError.getLinePosition() : null;
        this.updateLineStyles(oldPosition, newPosition, newPositionStyles);
    }

    private void updateLineStyles(Integer oldPosition, Integer newPosition, Collection<String> newPositionStyles) {
        if (oldPosition != null) {
            this.codeArea.setParagraphStyle(oldPosition, DEFAULT_LINE_STYLES);
        }

        if (newPosition != null) {
            this.codeArea.setParagraphStyle(newPosition, newPositionStyles);
        }
    }

}
