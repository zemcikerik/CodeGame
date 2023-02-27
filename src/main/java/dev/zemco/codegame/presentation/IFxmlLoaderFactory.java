package dev.zemco.codegame.presentation;

import javafx.fxml.FXMLLoader;

/**
 * Produces JavaFX {@link FXMLLoader fxml loaders} ready for loading of views.
 * @author Erik Zemčík
 */
public interface IFxmlLoaderFactory {

    /**
     * Creates a JavaFX FXML loader ready for use.
     * @return fxml loader
     */
    FXMLLoader createFxmlLoader();

}
