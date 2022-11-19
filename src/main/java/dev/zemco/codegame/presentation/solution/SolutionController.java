package dev.zemco.codegame.presentation.solution;

import dev.zemco.codegame.presentation.errors.IProgramErrorModel;
import dev.zemco.codegame.problems.Problem;
import dev.zemco.codegame.util.BindingUtils;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.collection.ListModification;

import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class SolutionController implements Initializable {

    @FXML
    private Label problemNameLabel;

    @FXML
    private CodeArea codeArea;

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
    private final IHighlightStyleComputer highlightStyleComputer;

    public SolutionController(ISolutionModel model, IHighlightStyleComputer highlightStyleComputer) {
        this.model = checkArgumentNotNull(model, "Model");
        this.highlightStyleComputer = checkArgumentNotNull(highlightStyleComputer, "Highlight style computer");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.problemNameLabel.textProperty().bind(
                BindingUtils.mapOrNull(this.model.problemProperty(), Problem::getName)
        );

        this.codeArea.disableProperty().bind(this.model.executionRunningProperty());
        this.codeArea.getParagraphs().addModificationObserver(this::onCodeAreaParagraphChanged);
        this.codeArea.setParagraphGraphicFactory(LineNumberFactory.get(this.codeArea));
        this.codeArea.setMouseOverTextDelay(Duration.ofSeconds(1));

        this.compileButton.disableProperty().bind(Bindings.not(this.model.canCompileProperty()));

        this.toggleExecutionButton.disableProperty().bind(Bindings.not(this.model.canExecuteProperty()));
        this.toggleExecutionButton.textProperty().bind(
                Bindings.when(this.model.executionRunningProperty())
                        .then("Stop")
                        .otherwise("Start")
        );

        this.stepButton.disableProperty().bind(Bindings.not(this.model.canStepProperty()));

        // TODO: weak
        this.model.syntaxErrorProperty().addListener(this::onSyntaxErrorModelChanged);
    }

    @FXML
    private void onCodeAreaTextChanged() {
        this.model.resetAttempt();
    }

    @FXML
    private void onCompileButtonClicked() {
        this.model.submitAttemptForCompilation(this.codeArea.getText());
    }

    @FXML
    private void onToggleExecutionButtonClicked() {
        if (this.model.executionRunningProperty().get()) {
            this.model.stopExecution();
        } else {
            this.model.startExecution();
        }
    }

    // TODO: fix me
    private void onCodeAreaParagraphChanged(
            ListModification<? extends Paragraph<Collection<String>, String, Collection<String>>> modification
    ) {
        // prevent triggering of this observer by changes done by this observer
        if (modification.getAddedSize() == 0) {
            return;
        }

        int paragraph = modification.getFrom();
        int paragraphLength = this.codeArea.getParagraphLength(paragraph);
        String modified = this.codeArea.getText(paragraph, 0, paragraph, paragraphLength);

        StyleSpans<Collection<String>> styles = this.highlightStyleComputer.computeHighlightStyles(modified);
        int paragraphStartIndex = this.codeArea.getAbsolutePosition(paragraph, 0);

        this.codeArea.setStyleSpans(paragraphStartIndex, styles);
    }

    private void onSyntaxErrorModelChanged(Object ignored, IProgramErrorModel oldError, IProgramErrorModel newError) {
        if (oldError != null) {
            this.codeArea.setParagraphStyle(oldError.getLinePosition(), Collections.emptyList());
        }

        if (newError != null) {
            this.codeArea.setParagraphStyle(newError.getLinePosition(), List.of("syntax-error"));
        }
    }

}
