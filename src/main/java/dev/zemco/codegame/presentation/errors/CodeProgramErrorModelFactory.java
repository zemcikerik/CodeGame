package dev.zemco.codegame.presentation.errors;

import dev.zemco.codegame.compilation.InvalidSyntaxException;
import dev.zemco.codegame.execution.StepExecutionException;

public class CodeProgramErrorModelFactory implements IProgramErrorModelFactory {

    @Override
    public IProgramErrorModel createProgramErrorModel(InvalidSyntaxException exception) {
        return new ImmutableProgramErrorModel(this.createDescription(exception), exception.getLinePosition());
    }

    @Override
    public IProgramErrorModel createProgramErrorModel(StepExecutionException exception) {
        return new ImmutableProgramErrorModel(this.createDescription(exception), exception.getLinePosition());
    }

    private String createDescription(Exception exception) {
        StringBuilder builder = new StringBuilder();

        for (Throwable e = exception; e != null; e = e.getCause()) {
            builder.append(e.getMessage());
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

}
