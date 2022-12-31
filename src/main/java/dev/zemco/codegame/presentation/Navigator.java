package dev.zemco.codegame.presentation;

import javafx.scene.Parent;
import javafx.stage.Stage;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class Navigator implements INavigator {

    private final IStageProvider stageProvider;
    private final IViewProvider viewProvider;

    public Navigator(IStageProvider stageProvider, IViewProvider viewProvider) {
        this.stageProvider = checkArgumentNotNull(stageProvider, "Stage provider");
        this.viewProvider = checkArgumentNotNull(viewProvider, "View provider");
    }

    @Override
    public void navigateTo(String viewId) {
        Stage primaryStage = this.stageProvider.getPrimaryStage();
        Parent view = this.viewProvider.getViewById(viewId);
        primaryStage.getScene().setRoot(view);
    }

}
