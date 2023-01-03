package dev.zemco.codegame.presentation.errors;

import dev.zemco.codegame.compilation.InvalidSyntaxException;
import dev.zemco.codegame.evaluation.StepEvaluationException;
import dev.zemco.codegame.execution.engine.StepExecutionException;

public class CodeProgramErrorModelFactory implements IProgramErrorModelFactory {

    @Override
    public IProgramErrorModel createProgramErrorModel(InvalidSyntaxException exception) {
        return new ImmutableProgramErrorModel(this.createDescription(exception), exception.getLinePosition());
    }

    @Override
    public IProgramErrorModel createProgramErrorModel(StepEvaluationException exception) {
        Throwable cause = exception.getCause();

        return cause != null
            ? this.createProgramErrorModelFromEvaluationCause(cause)
            : new ImmutableProgramErrorModel(this.createDescription(exception), null);
    }

    private IProgramErrorModel createProgramErrorModelFromEvaluationCause(Throwable cause) {
        Integer linePosition = cause instanceof StepExecutionException
            ? ((StepExecutionException)cause).getLinePosition()
            : null;

        return new ImmutableProgramErrorModel(this.createDescription(cause), linePosition);
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
