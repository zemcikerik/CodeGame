package dev.zemco.codegame.presentation.dialog;

/**
 * Service for presenting popup dialogs to the user.
 * Dialogs are synchronous, and should block the calling {@link Thread thread} until they are closed by the user.
 * <p>
 * Their implementation should preferably present dialogs separately from the main content of the application,
 * preferably as a modal window.
 *
 * @author Erik Zemčík
 */
public interface IDialogService {

    /**
     * Displays an error alert dialog to the user.
     * Blocks the current {@link Thread thread} until it is closed.
     *
     * @param title title of the dialog
     * @param message message of the dialog
     *
     * @throws IllegalArgumentException if {@code title} is {@code null} or if {@code message} is {@code null}
     */
    void showErrorDialog(String title, String message);

    /**
     * Displays an information alert dialog to the user.
     * Blocks the current {@link Thread thread} until it is closed.
     *
     * @param title title of the dialog
     * @param message message of the dialog
     *
     * @throws IllegalArgumentException if {@code title} is {@code null} or if {@code message} is {@code null}
     */
    void showInformationDialog(String title, String message);

}
