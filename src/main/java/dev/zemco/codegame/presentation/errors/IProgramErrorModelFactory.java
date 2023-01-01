package dev.zemco.codegame.presentation.errors;

import dev.zemco.codegame.compilation.InvalidSyntaxException;
import dev.zemco.codegame.execution.NoNextInstructionException;
import dev.zemco.codegame.execution.StepExecutionException;

public interface IProgramErrorModelFactory {
    IProgramErrorModel createProgramErrorModel(InvalidSyntaxException exception);
    IProgramErrorModel createProgramErrorModel(StepExecutionException exception);
    IProgramErrorModel createProgramErrorModel(NoNextInstructionException exception);
}
