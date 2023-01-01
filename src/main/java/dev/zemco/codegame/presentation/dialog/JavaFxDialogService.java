package dev.zemco.codegame.presentation.dialog;

import javafx.scene.control.Alert;

public class JavaFxDialogService implements IDialogService {

    @Override
    public void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
