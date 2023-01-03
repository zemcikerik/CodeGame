package dev.zemco.codegame.presentation.errors;

import dev.zemco.codegame.compilation.InvalidSyntaxException;
import dev.zemco.codegame.evaluation.StepEvaluationException;

public interface IProgramErrorModelFactory {
    IProgramErrorModel createProgramErrorModel(InvalidSyntaxException exception);
    IProgramErrorModel createProgramErrorModel(StepEvaluationException exception);
}
