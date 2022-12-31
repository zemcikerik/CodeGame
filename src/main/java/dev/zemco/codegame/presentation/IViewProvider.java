package dev.zemco.codegame.presentation;

import javafx.scene.Parent;

public interface IViewProvider {
    Parent getViewById(String viewId);
}
