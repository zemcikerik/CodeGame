package dev.zemco.codegame.presentation.solution.errors;

import dev.zemco.codegame.compilation.InvalidSyntaxException;
import dev.zemco.codegame.evaluation.StepEvaluationException;
import dev.zemco.codegame.execution.engine.StepExecutionException;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Simple implementation that formats error model descriptions by joining
 * the {@link Exception#getMessage() messages} of the exception {@link Exception#getCause() chain}.
 *
 * @author Erik Zemčík
 */
public class SimpleSolutionErrorModelFactory implements ISolutionErrorModelFactory {

    @Override
    public ISolutionErrorModel createSolutionErrorModel(InvalidSyntaxException exception) {
        checkArgumentNotNull(exception, "Exception");
        return new ImmutableSolutionErrorModel(this.createDescription(exception), exception.getLinePosition());
    }

    @Override
    public ISolutionErrorModel createSolutionErrorModel(StepEvaluationException exception) {
        checkArgumentNotNull(exception, "Exception");

        Throwable cause = exception.getCause();

        if (cause == null) {
            // fallback to thrown exception if no root cause available
            return new ImmutableSolutionErrorModel(this.createDescription(exception), null);
        }

        Integer linePosition = cause instanceof StepExecutionException stepExecutionCause
            ? stepExecutionCause.getLinePosition()
            : null;

        return new ImmutableSolutionErrorModel(this.createDescription(cause), linePosition);
    }

    private String createDescription(Throwable throwable) {
        StringBuilder builder = new StringBuilder();

        for (Throwable t = throwable; t != null; t = t.getCause()) {
            builder.append(t.getMessage());
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

}
