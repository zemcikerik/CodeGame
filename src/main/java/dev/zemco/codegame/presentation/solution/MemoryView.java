package dev.zemco.codegame.presentation.solution;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MemoryView<T> extends TableView<T> implements Initializable {

    @FXML
    private TableView<?> memoryTable;

    @FXML
    private TableColumn<?, ?> addressColumn;

    @FXML
    private TableColumn<?, ?> valueColumn;

    public MemoryView() throws IOException {
        FXMLLoader loader = new FXMLLoader(MemoryView.class.getResource("/fxml/MemoryControl.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.addressColumn.prefWidthProperty().bind(
                this.memoryTable.widthProperty().divide(2)
        );

        this.valueColumn.prefWidthProperty().bind(
                this.memoryTable.widthProperty().divide(2)
        );
    }

}
