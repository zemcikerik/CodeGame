package dev.zemco.codegame.presentation.errors;

public class ImmutableProgramErrorModel implements IProgramErrorModel {

    private final String description;
    private final int linePosition;

    public ImmutableProgramErrorModel(String description, int linePosition) {
        // TODO: validate
        this.description = description;
        this.linePosition = linePosition;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getLinePosition() {
        return this.linePosition;
    }

}
