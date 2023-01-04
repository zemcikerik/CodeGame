package dev.zemco.codegame.presentation;

/**
 * Handles navigation between routes of the application.
 * Each navigator has its own internal routing rules, which are applied
 * when navigation to a specific route is requested.
 * Successful navigation looks like a shift to a different view from the perspective of the user.
 *
 * @author Erik Zemčík
 */
public interface INavigator {

    /**
     * Performs a navigation to the specified route.
     * @param route route to navigate to
     * @throws IllegalArgumentException if {@code route} is {@code null}
     * @throws UnknownRouteException if {@code route} is not known by the navigator
     */
    void navigateTo(String route);

}
