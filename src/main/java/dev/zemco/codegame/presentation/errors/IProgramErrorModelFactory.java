package dev.zemco.codegame.presentation.errors;

import dev.zemco.codegame.compilation.InvalidSyntaxException;

public interface IProgramErrorModelFactory {
    IProgramErrorModel createProgramErrorModel(InvalidSyntaxException exception);
}
