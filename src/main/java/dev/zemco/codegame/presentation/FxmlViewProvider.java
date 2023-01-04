package dev.zemco.codegame.presentation;

import dev.zemco.codegame.resources.ResourceLoadException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Provides JavaFX views by loading raw FXML views from the {@link IFxmlViewSourceProvider}.
 * These views are controlled using controllers created by the {@link IControllerFactory},
 * and styles with stylesheets provided by the {@link IViewStylesheetProvider}.
 *
 * @author Erik Zemčík
 * @see IFxmlViewSourceProvider
 */
public class FxmlViewProvider implements IViewProvider {

    private final IFxmlViewSourceProvider viewSourceProvider;
    private final IControllerFactory controllerFactory;
    private final IViewStylesheetProvider viewStylesheetProvider;

    /**
     * Creates an instance of {@link FxmlViewProvider} ready for use.
     *
     * @param viewSourceProvider raw fxml view source provider for the sources of views
     * @param controllerFactory factory used to create controllers for views
     * @param viewStylesheetProvider view stylesheet provider used as a source of needed styles for views
     *
     * @throws IllegalArgumentException if any argument is {@code null}
     */
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

    /**
     * Provides the root node of the requested view ready to be presented.
     * The view is identified by the {@code viewId} parameter. This parameter is used by the internal providers
     * specified during instantiation for providing the required parts of the view.
     *
     * @param viewId identification of the view
     * @return root node of the view
     *
     * @throws IllegalArgumentException if {@code viewId} is {@code null} or empty
     * @throws UnknownViewException if the view is not known by the used
     *                              {@link IFxmlViewSourceProvider fxml view source provider} or the
     *                              {@link IViewStylesheetProvider stylesheet provider}
     * @throws ResourceLoadException if the raw fxml view source couldn't be loaded
     */
    @Override
    public Parent getViewById(String viewId) {
        checkArgumentNotEmpty(viewId, "View id");

        InputStream fxmlSource = this.viewSourceProvider.getFxmlViewSourceStreamById(viewId);
        Parent view = this.loadViewFromSource(fxmlSource);

        List<String> stylesheets = this.viewStylesheetProvider.getStylesheetsByViewId(viewId);
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
