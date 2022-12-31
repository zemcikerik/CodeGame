package dev.zemco.codegame.presentation;

import dev.zemco.codegame.problems.ResourceLoadException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

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
        this.viewStylesheetProvider = checkArgumentNotNull(
            viewStylesheetProvider, "View stylesheet provider"
        );
    }

    @Override
    public Parent getViewById(String viewId) {
        InputStream fxmlSource = this.viewSourceProvider.getFxmlViewSourceById(viewId);
        List<String> stylesheets = this.viewStylesheetProvider.getStylesheetsByViewId(viewId);
        Parent view = this.loadViewFromSource(fxmlSource);
        view.getStylesheets().addAll(stylesheets);
        return view;
    }

    private Parent loadViewFromSource(InputStream fxmlSource) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(this.controllerFactory::createController);

        try {
            return fxmlLoader.load(fxmlSource);
        } catch (IOException e) {
            throw new ResourceLoadException("Failed to load view from FXML!", e);
        }
    }

}
