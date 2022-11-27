package dev.zemco.codegame.presentation;

import javafx.scene.Scene;

public interface IViewProvider {
    // TODO: don't use entire scenes as roots for views
    Scene getViewById(String viewId);
}
