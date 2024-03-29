package dev.zemco.codegame.presentation.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Implementation of a service for presenting popup dialogs to the user using JavaFX.
 * They are presented in a separate modal window synchronously by blocking the calling {@link Thread thread} until
 * they are closed by the user.
 * <p>
 * For simple alerts ({@link #showErrorDialog(String, String) error} and
 * {@link #showInformationDialog(String, String) information} types), the JavaFX class {@link Alert}
 * with appropriate {@link AlertType} was used.
 *
 * @author Erik Zemčík
 * @see Alert
 */
public class JavaFxDialogService implements IDialogService {

    private final IAlertFactory alertFactory;

    /**
     * Creates an instance of {@link JavaFxDialogService} that presents popup dialogs to user using JavaFX.
     * @param alertFactory source of alerts
     * @throws IllegalArgumentException if {@code alertFactory} is {@code null}
     */
    public JavaFxDialogService(IAlertFactory alertFactory) {
        this.alertFactory = checkArgumentNotNull(alertFactory, "Alert factory");
    }

    @Override
    public void showErrorDialog(String title, String message) {
        this.showDialog(AlertType.ERROR, title, message);
    }

    @Override
    public void showInformationDialog(String title, String message) {
        this.showDialog(AlertType.INFORMATION, title, message);
    }

    private void showDialog(AlertType type, String title, String message) {
        checkArgumentNotNull(title, "Title");
        checkArgumentNotNull(message, "Message");

        Alert alert = this.alertFactory.createAlert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

