package dev.zemco.codegame.execution.io;

// TODO: remove me
public class NoOpOutputSink implements OutputSink {

    @Override
    public void accept(int value) {
        System.out.format("[Output Sink] Value: %d%n", value);
    }

    @Override
    public boolean isSatisfied() {
        return false;
    }

}
