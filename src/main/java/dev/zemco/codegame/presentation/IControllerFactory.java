package dev.zemco.codegame.presentation;

/**
 * Produces controllers for the JavaFX views.
 * @author Erik Zemčík
 */
public interface IControllerFactory {

    /**
     * Creates a controller for a JavaFX view based on the provided controller class.
     * The controller should be the instance of the controller class or any of its subclass.
     *
     * @param controllerClass class of the controller to create
     * @return controller
     *
     * @throws IllegalArgumentException if {@code controllerClass} is {@code null}
     */
    Object createController(Class<?> controllerClass);

}
