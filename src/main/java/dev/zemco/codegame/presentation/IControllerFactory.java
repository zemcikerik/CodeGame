package dev.zemco.codegame.presentation;

@FunctionalInterface
public interface IControllerFactory {
    Object createController(Class<?> controllerClass);
}
