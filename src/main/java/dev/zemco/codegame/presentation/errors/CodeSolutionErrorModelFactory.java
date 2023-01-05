package dev.zemco.codegame.presentation.errors;

import dev.zemco.codegame.compilation.InvalidSyntaxException;
import dev.zemco.codegame.evaluation.StepEvaluationException;
import dev.zemco.codegame.execution.engine.StepExecutionException;

public class CodeSolutionErrorModelFactory implements ISolutionErrorModelFactory {

    @Override
    public ISolutionErrorModel createSolutionErrorModel(InvalidSyntaxException exception) {
        return new ImmutableSolutionErrorModel(this.createDescription(exception), exception.getLinePosition());
    }

    @Override
    public ISolutionErrorModel createSolutionErrorModel(StepEvaluationException exception) {
        Throwable cause = exception.getCause();

        Integer linePosition = cause instanceof StepExecutionException
            ? ((StepExecutionException)cause).getLinePosition()
            : null;

        return new ImmutableSolutionErrorModel(this.createDescription(cause), linePosition);
    }

    private String createDescription(Throwable exception) {
        StringBuilder builder = new StringBuilder();

        for (Throwable e = exception; e != null; e = e.getCause()) {
            builder.append(e.getMessage());
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

}
