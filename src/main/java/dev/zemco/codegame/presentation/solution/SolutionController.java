package dev.zemco.codegame.presentation.solution;

import javafx.beans.property.SimpleBooleanProperty;
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

public class SolutionController implements Initializable {

    @FXML
    private Text levelNameText;

    @FXML
    private CodeArea codeArea;

    @FXML
    private Button compileButton;

    @FXML
    private Button startButton;

    @FXML
    private Button stepButton;

    @FXML
    private CheckBox autoEnabledCheckbox;

    @FXML
    private Slider autoSpeedSlider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var test = new SimpleBooleanProperty(false);
        this.compileButton.disableProperty().bind(test);
        this.startButton.disableProperty().bind(test.not());
        this.codeArea.setParagraphGraphicFactory(LineNumberFactory.get(this.codeArea));
    }

    @FXML
    public void onCompileButtonClicked() {
        System.out.println(this.codeArea.getText());
    }

}
