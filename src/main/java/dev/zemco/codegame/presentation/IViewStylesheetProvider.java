package dev.zemco.codegame.presentation;

import java.util.List;

/**
 * Manages the stylesheets for JavaFX views.
 * @author Erik Zemčík
 */
public interface IViewStylesheetProvider {

    /**
     * Provides the string URLs linking to the stylesheets assigned to the requested view.
     * These stylesheets are usable by the content of the JavaFX {@link javafx.scene.Scene scene}.
     * The view is identified by the {@code viewId} parameter.
     *
     * @param viewId identification of the view
     * @return unmodifiable {@link List list} of stylesheets
     *
     * @throws IllegalArgumentException if {@code viewId} is {@code null} or empty
     * @throws UnknownViewException if the view is not known by the provider
     *
     * @see javafx.scene.Scene#getStylesheets()
     */
    List<String> getStylesheetsByViewId(String viewId);

}
