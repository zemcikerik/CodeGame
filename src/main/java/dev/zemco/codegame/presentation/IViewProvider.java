package dev.zemco.codegame.presentation;

import javafx.scene.Scene;

public interface IViewProvider {
    Scene getViewById(String viewId);
}
