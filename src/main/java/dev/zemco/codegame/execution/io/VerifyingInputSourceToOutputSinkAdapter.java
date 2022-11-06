package dev.zemco.codegame.execution.io;

// TODO: tests
public class VerifyingInputSourceToOutputSinkAdapter implements OutputSink {

    private final InputSource inputSource;

    public VerifyingInputSourceToOutputSinkAdapter(InputSource inputSource) {
        if (inputSource == null) {
            throw new IllegalArgumentException("Input source cannot be null!");
        }
        this.inputSource = inputSource;
    }

    @Override
    public void accept(int value) {
        if (this.isSatisfied()) {
            throw new NotAcceptedException("Output sink is already satisfied!");
        }
        int expected = this.inputSource.getNextValue();
        if (expected != value) {
            String message = String.format("Output sink rejected value %d as it expected value %d!", value, expected);
            throw new NotAcceptedException(message);
        }
    }

    @Override
    public boolean isSatisfied() {
        return !this.inputSource.hasNextValue();
    }

}
