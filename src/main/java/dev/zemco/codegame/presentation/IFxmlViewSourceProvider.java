package dev.zemco.codegame.presentation;

import java.io.InputStream;

/**
 * Manages the JavaFX FXML sources for views.
 * @author Erik Zemčík
 */
public interface IFxmlViewSourceProvider {

    /**
     * Provides an {@link InputStream input stream} with the raw FXML source of the requested view.
     * The view is identified by the {@code viewId} parameter.
     *
     * @param viewId identification of the view
     * @return input stream with the raw FXML source
     *
     * @throws IllegalArgumentException if {@code viewId} is {@code null} or empty
     * @throws UnknownViewException when the view is not known by the provider
     */
    InputStream getFxmlViewSourceStreamById(String viewId);

}
