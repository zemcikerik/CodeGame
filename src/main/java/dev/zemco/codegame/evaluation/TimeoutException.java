package dev.zemco.codegame.evaluation;

/**
 * Thrown during the evaluation step if maximum allowed number of steps was reached.
 * @author Erik Zemčík
 */
public class TimeoutException extends StepEvaluationException {

    /**
     * Creates an instance of {@link TimeoutException}.
     * @param message detail message containing information about the state of evaluation
     */
    public TimeoutException(String message) {
        super(message);
    }

}
