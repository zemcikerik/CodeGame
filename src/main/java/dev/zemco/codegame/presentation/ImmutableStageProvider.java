package dev.zemco.codegame.presentation;

import javafx.stage.Stage;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class ImmutableStageProvider implements IStageProvider {

    private final Stage primaryStage;

    public ImmutableStageProvider(Stage primaryStage) {
        this.primaryStage = checkArgumentNotNull(primaryStage, "Primary stage");
    }

    @Override
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

}
