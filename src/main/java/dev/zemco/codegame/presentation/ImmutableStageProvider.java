package dev.zemco.codegame.presentation;

import javafx.stage.Stage;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Immutable provider for JavaFX {@link Stage stages} used by the application for presentation.
 * @author Erik Zemčík
 */
public class ImmutableStageProvider implements IStageProvider {

    private final Stage primaryStage;

    /**
     * Creates an instance of {@link ImmutableStageProvider} that holds the given primary stage.
     * @param primaryStage primary stage to provide
     * @throws IllegalArgumentException if {@code primaryStage} is {@code null}
     */
    public ImmutableStageProvider(Stage primaryStage) {
        this.primaryStage = checkArgumentNotNull(primaryStage, "Primary stage");
    }

    @Override
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

}
