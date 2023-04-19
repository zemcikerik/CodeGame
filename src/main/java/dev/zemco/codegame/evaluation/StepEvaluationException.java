package dev.zemco.codegame.evaluation;

/**
 * Thrown during the evaluation step if the evaluation reaches an undesired state.
 * @author Erik Zemčík
 */
public class StepEvaluationException extends RuntimeException {

    /**
     * Creates an instance of {@link StepEvaluationException}.
     * @param message detail message containing information about the state of the evaluation
     */
    public StepEvaluationException(String message) {
        super(message);
    }

    /**
     * Creates an instance of {@link StepEvaluationException}.
     *
     * @param message detail message containing information about the state of the evaluation
     * @param cause cause of the undesired state
     */
    public StepEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }

}
