package dev.zemco.codegame.presentation.solution;

import dev.zemco.codegame.problems.Problem;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class SolutionController implements Initializable {

    @FXML
    private Text problemNameText;

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
        Problem problem = this.model.getProblem();
        this.problemNameText.setText(problem.getName());

        this.codeArea.setParagraphGraphicFactory(LineNumberFactory.get(this.codeArea));
        this.codeArea.disableProperty().bind(this.model.executionRunningProperty());

        // TODO: ???
        this.codeArea.getStylesheets().add("/css/SolutionView.css");
        this.codeArea.getParagraphs().addModificationObserver(modification -> {
            // prevent triggering of this observer by changes done by this observer
            if (modification.getAddedSize() == 0) {
                return;
            }

            int paragraph = modification.getFrom();
            int paragraphLength = this.codeArea.getParagraphLength(paragraph);
            String modified = this.codeArea.getText(paragraph, 0, paragraph, paragraphLength);

            this.codeArea.setStyleSpans(
                    this.codeArea.getAbsolutePosition(paragraph, 0),
                    this.highlightStyleComputer.computeHighlightStyles(modified)
            );
        });

        this.compileButton.disableProperty().bind(Bindings.not(this.model.canCompileProperty()));

        this.toggleExecutionButton.disableProperty().bind(Bindings.not(this.model.canExecuteProperty()));
        this.toggleExecutionButton.textProperty().bind(
                Bindings.when(this.model.executionRunningProperty())
                        .then("Stop")
                        .otherwise("Start")
        );

        this.stepButton.disableProperty().bind(Bindings.not(this.model.canStepProperty()));
    }

    @FXML
    public void onCodeAreaTextChanged() {
        this.model.resetAttempt();
    }

    @FXML
    public void onCompileButtonClicked() {
        this.model.submitAttemptForCompilation(this.codeArea.getText());
    }

    @FXML
    public void onToggleExecutionButtonClicked() {
        if (this.model.executionRunningProperty().get()) {
            this.model.stopExecution();
        } else {
            this.model.startExecution();
        }
    }

}
