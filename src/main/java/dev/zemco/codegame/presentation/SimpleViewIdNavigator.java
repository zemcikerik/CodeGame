package dev.zemco.codegame.presentation;

import javafx.scene.Parent;
import javafx.stage.Stage;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Simple implementation of navigation which treats requested navigation routes as view ids.
 * The navigation is performed by switching the root of a scene of the primary stage of the application.
 *
 * @author Erik Zemčík
 */
public class SimpleViewIdNavigator implements INavigator {

    private final IStageProvider stageProvider;
    private final IViewProvider viewProvider;

    /**
     * Creates an instance of {@link SimpleViewIdNavigator} ready for navigation.
     *
     * @param stageProvider source of the primary stage of the application
     * @param viewProvider source of the navigation destination views of the application
     *
     * @throws IllegalArgumentException if {@code stageProvider} is {@code null} or
     *                                  if {@code viewProvider} is {@code null}
     */
    public SimpleViewIdNavigator(IStageProvider stageProvider, IViewProvider viewProvider) {
        this.stageProvider = checkArgumentNotNull(stageProvider, "Stage provider");
        this.viewProvider = checkArgumentNotNull(viewProvider, "View provider");
    }

    /**
     * Switches the view of the primary stage to the view id specified by the {@code route} parameter.
     * @param route identification of the view to navigate to
     *
     * @throws IllegalArgumentException if {@code route} is {@code null} or empty
     * @throws UnknownRouteException if {@code route} is not a known view id
     *
     * @see IStageProvider#getPrimaryStage()
     * @see IViewProvider#getViewById(String)
     */
    @Override
    public void navigateTo(String route) {
        checkArgumentNotEmpty(route, "View id");

        Stage primaryStage = this.stageProvider.getPrimaryStage();
        Parent view = this.getDestinationViewById(route);
        primaryStage.getScene().setRoot(view);
    }

    private Parent getDestinationViewById(String viewId) {
        try {
            return this.viewProvider.getViewById(viewId);
        } catch (UnknownViewException e) {
            String message = String.format("Route '%s' is not known by the navigator!", viewId);
            throw new UnknownRouteException(message, e);
        }
    }

}
