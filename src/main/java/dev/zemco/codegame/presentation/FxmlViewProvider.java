package dev.zemco.codegame.presentation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.InputStream;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class FxmlViewProvider implements IViewProvider {

    private final IViewSourceProvider viewSourceProvider;
    private final FXMLLoader fxmlLoader;

    public FxmlViewProvider(IViewSourceProvider viewSourceProvider, IControllerFactory controllerFactory) {
        this.viewSourceProvider = checkArgumentNotNull(viewSourceProvider, "View source provider");
        checkArgumentNotNull(controllerFactory, "Controller factory");

        this.fxmlLoader = new FXMLLoader();
        this.fxmlLoader.setControllerFactory(controllerFactory::createController);
    }

    @Override
    public Scene getViewByName(String viewName) {
        // TODO: is this fine?
        InputStream source = this.viewSourceProvider.getViewSourceByName(viewName);

        try {
            return this.fxmlLoader.load(source);
        } catch (IOException e) {
            // TODO: handle properly
            throw new RuntimeException(e);
        }
    }

}
