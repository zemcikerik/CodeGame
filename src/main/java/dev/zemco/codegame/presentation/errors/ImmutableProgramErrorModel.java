package dev.zemco.codegame.presentation.errors;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

public class ImmutableProgramErrorModel implements IProgramErrorModel {

    private final String description;
    private final int linePosition;

    public ImmutableProgramErrorModel(String description, int linePosition) {
        this.description = checkArgumentNotNull(description, "Description");
        this.linePosition = checkArgumentPositiveInteger(linePosition, "Line position");
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
