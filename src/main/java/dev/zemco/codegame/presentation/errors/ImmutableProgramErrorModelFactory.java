package dev.zemco.codegame.presentation.errors;

import dev.zemco.codegame.compilation.InvalidSyntaxException;

// TODO: ???
public class ImmutableProgramErrorModelFactory implements IProgramErrorModelFactory {

    @Override
    public IProgramErrorModel createProgramErrorModel(InvalidSyntaxException exception) {
        return new ImmutableProgramErrorModel(exception.getMessage(), exception.getLinePosition());
    }

}
