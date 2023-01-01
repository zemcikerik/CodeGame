package dev.zemco.codegame.presentation.errors;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentPositiveInteger;

public class ImmutableProgramErrorModel implements IProgramErrorModel {

    private final String description;
    private final Integer linePosition;

    public ImmutableProgramErrorModel(String description, Integer linePosition) {
        this.description = checkArgumentNotNull(description, "Description");

        if (linePosition != null) {
            checkArgumentPositiveInteger(linePosition, "Line position");
        }

        this.linePosition = linePosition;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Integer getLinePosition() {
        return this.linePosition;
    }

}
