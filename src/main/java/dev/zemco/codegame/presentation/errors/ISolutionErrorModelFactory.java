package dev.zemco.codegame.presentation.errors;

import dev.zemco.codegame.compilation.InvalidSyntaxException;
import dev.zemco.codegame.evaluation.StepEvaluationException;

public interface ISolutionErrorModelFactory {
    ISolutionErrorModel createSolutionErrorModel(InvalidSyntaxException exception);
    ISolutionErrorModel createSolutionErrorModel(StepEvaluationException exception);
}
