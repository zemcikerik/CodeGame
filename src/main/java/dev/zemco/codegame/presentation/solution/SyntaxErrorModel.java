package dev.zemco.codegame.presentation.solution;

// TODO: interface, rename
public class SyntaxErrorModel {

    private final int linePosition;
    private final String description;

    public SyntaxErrorModel(int linePosition, String description) {
        // TODO: validate
        this.linePosition = linePosition;
        this.description = description;
    }

    public int getLinePosition() {
        return this.linePosition;
    }

    public String getDescription() {
        return this.description;
    }

}
