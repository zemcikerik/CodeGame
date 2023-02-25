package dev.zemco.codegame.presentation.dialog;

import javafx.scene.control.Alert;

/**
 * Produces JavaFX {@link Alert alerts}.
 * @author Erik Zemčík
 */
public interface IAlertFactory {

    /**
     * Creates a JavaFX alert of a provided type.
     * @param type alert type
     * @return alert
     * @throws IllegalArgumentException if {@code type} is {@code null}
     */
    Alert createAlert(Alert.AlertType type);

}
