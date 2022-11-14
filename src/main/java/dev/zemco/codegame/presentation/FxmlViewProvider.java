package dev.zemco.codegame.presentation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class FxmlViewProvider implements IViewProvider {

    private final FXMLLoader fxmlLoader;

    public FxmlViewProvider(IControllerFactory controllerFactory) {
        checkArgumentNotNull(controllerFactory, "Controller factory");

        this.fxmlLoader = new FXMLLoader();
        this.fxmlLoader.setControllerFactory(controllerFactory::createController);
    }

    @Override
    public Scene getViewByName(String viewName) {
        // TODO: implement
        throw new RuntimeException("Not implemented!");
    }

}
