package dev.zemco.codegame.presentation;

import javafx.scene.Parent;

/**
 * Manages the JavaFX views used by application for presentation.
 * These views are typically displayed as roots/children of {@link javafx.scene.Scene scenes}.
 *
 * @author Erik Zemčík
 */
public interface IViewProvider {

    /**
     * Provides the root node of the requested view ready to be presented.
     * The view is identified by the {@code viewId} parameter.
     *
     * @param viewId identification of the view
     * @return root node of the view
     *
     * @throws IllegalArgumentException if {@code viewId} is {@code null} or empty
     * @throws UnknownViewException if the view is not known by the provider
     */
    Parent getViewById(String viewId);

}
