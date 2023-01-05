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

        // step evaluation exception always has a root cause (check javadoc)
        Throwable cause = exception.getCause();

        Integer linePosition = cause instanceof StepExecutionException
            ? ((StepExecutionException)cause).getLinePosition()
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
