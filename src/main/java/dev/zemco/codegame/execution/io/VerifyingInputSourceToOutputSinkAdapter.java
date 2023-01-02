package dev.zemco.codegame.execution.io;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * {@link IOutputSink Output sink} that matches execution outputs to values from {@link IInputSource input source}.
 * <p>
 * This output sink is only {@link #isSatisfied() satisfied} if output values exactly match values from the adapted
 * {@link IInputSource input source}.
 *
 * @author Erik Zemčík
 * @see IInputSource
 */
public class VerifyingInputSourceToOutputSinkAdapter implements IOutputSink {

    private final IInputSource inputSource;

    /**
     * Creates an instance of {@link VerifyingInputSourceToOutputSinkAdapter}
     * by adapting existing {@link IInputSource input source}.
     *
     * @param inputSource {@link IInputSource input source} to adapt
     */
    public VerifyingInputSourceToOutputSinkAdapter(IInputSource inputSource) {
        this.inputSource = checkArgumentNotNull(inputSource, "Input source");
    }

    /**
     * Attempts to match output {@code value} to next value from the adapted {@link IInputSource input source}.
     *
     * @param value value to match
     * @throws NotAcceptedException if adapted {@link IInputSource input source} has no next value or
     *                              if next value from {@link IInputSource input source} does not match
     *                              output {@code value}
     */
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

    /**
     * Returns if the adapted {@link IInputSource input source} has no next value available.
     * @return true if the adapted {@link IInputSource input source} has no next value available, else false
     */
    @Override
    public boolean isSatisfied() {
        return !this.inputSource.hasNextValue();
    }

}
