package dev.zemco.codegame.presentation.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class JavaFxDialogService implements IDialogService {

    @Override
    public void showErrorDialog(String title, String message) {
        this.showDialog(AlertType.ERROR, title, message);
    }

    @Override
    public void showInformationDialog(String title, String message) {
        this.showDialog(AlertType.INFORMATION, title, message);
    }

    private void showDialog(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
