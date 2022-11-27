package dev.zemco.codegame.presentation.errors;

import dev.zemco.codegame.compilation.InvalidSyntaxException;

// TODO: rename me, add logic to figure out cause of the problem that is presentable to user
public class ImmutableProgramErrorModelFactory implements IProgramErrorModelFactory {

    @Override
    public IProgramErrorModel createProgramErrorModel(InvalidSyntaxException exception) {
        return new ImmutableProgramErrorModel(exception.getMessage(), exception.getLinePosition());
    }

}
