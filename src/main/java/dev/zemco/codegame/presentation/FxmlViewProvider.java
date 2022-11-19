package dev.zemco.codegame.presentation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class FxmlViewProvider implements IViewProvider {

    private final IFxmlViewSourceProvider viewSourceProvider;
    private final IControllerFactory controllerFactory;
    private final IViewStylesheetProvider viewStylesheetProvider;

    public FxmlViewProvider(
            IFxmlViewSourceProvider viewSourceProvider,
            IControllerFactory controllerFactory,
            IViewStylesheetProvider viewStylesheetProvider
    ) {
        this.viewSourceProvider = checkArgumentNotNull(viewSourceProvider, "View source provider");
        this.controllerFactory = checkArgumentNotNull(controllerFactory, "Controller factory");
        this.viewStylesheetProvider = checkArgumentNotNull(viewStylesheetProvider, "View stylesheet provider");
    }

    @Override
    public Scene getViewById(String viewId) {
        InputStream fxmlSource = this.viewSourceProvider.getFxmlViewSourceById(viewId);
        List<String> stylesheets = this.viewStylesheetProvider.getStylesheetsByViewId(viewId);
        Scene view = this.loadViewFromSource(fxmlSource);
        view.getStylesheets().addAll(stylesheets);
        return view;
    }

    private Scene loadViewFromSource(InputStream fxmlSource) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(this.controllerFactory::createController);

        try {
            return fxmlLoader.load(fxmlSource);
        } catch (IOException e) {
            // TODO: handle properly
            throw new RuntimeException(e);
        }
    }

}
