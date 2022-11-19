package dev.zemco.codegame.execution.io;

@FunctionalInterface
public interface IInputSourceFactory {
    IInputSource createInputSourceFromIterable(Iterable<Integer> iterable);
}
