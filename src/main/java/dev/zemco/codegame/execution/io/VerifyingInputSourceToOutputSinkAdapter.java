package dev.zemco.codegame.execution.io;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class VerifyingInputSourceToOutputSinkAdapter implements OutputSink {

    private final InputSource inputSource;

    public VerifyingInputSourceToOutputSinkAdapter(InputSource inputSource) {
        this.inputSource = checkArgumentNotNull(inputSource, "Input source");
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
