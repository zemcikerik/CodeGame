package dev.zemco.codegame.presentation.dialog;

public interface IDialogService {
    void showErrorDialog(String title, String message);
    void showInformationDialog(String title, String message);
}
