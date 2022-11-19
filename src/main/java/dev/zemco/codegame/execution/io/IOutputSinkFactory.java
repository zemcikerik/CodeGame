package dev.zemco.codegame.execution.io;

@FunctionalInterface
public interface IOutputSinkFactory {
    IOutputSink createVerifyingOutputSinkFromIterable(Iterable<Integer> iterable);
}
