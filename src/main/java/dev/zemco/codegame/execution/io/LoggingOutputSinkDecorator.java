package dev.zemco.codegame.execution.io;

public class LoggingOutputSinkDecorator implements IOutputSink {

    private final IOutputSink outputSink;

    public LoggingOutputSinkDecorator(IOutputSink outputSink) {
        this.outputSink = outputSink;
    }

    @Override
    public void accept(int value) {
        System.out.format("[Output Sink] Received value %d!%n", value);
        this.outputSink.accept(value);
    }

    @Override
    public boolean isSatisfied() {
        return this.outputSink.isSatisfied();
    }

}
