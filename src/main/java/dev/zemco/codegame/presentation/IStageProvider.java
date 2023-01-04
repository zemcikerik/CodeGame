package dev.zemco.codegame.presentation;

import javafx.stage.Stage;

/**
 * Manages the JavaFX {@link Stage stages} used by the application for presentation.
 *
 * @author Erik Zemčík
 * @see Stage
 */
public interface IStageProvider {

    /**
     * Provides the primary {@link Stage stage} of the application.
     * This {@link Stage stage} is typically used for main content presentation.
     *
     * @return primary stage
     */
    Stage getPrimaryStage();

}
