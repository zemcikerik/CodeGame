package dev.zemco.codegame.presentation;

import java.util.List;
import java.util.Map;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class ImmutableDefaultViewStylesheetProvider implements IViewStylesheetProvider {

    private final Map<String, List<String>> viewIdToStylesheetList;
    private final List<String> defaultStylesheets;

    public ImmutableDefaultViewStylesheetProvider(
            Map<String, List<String>> viewIdToStylesheetListMap,
            List<String> defaultStylesheets
    ) {
        // TODO: copy of inner lists?
        this.viewIdToStylesheetList = Map.copyOf(
            checkArgumentNotNull(viewIdToStylesheetListMap, "View id to stylesheet list")
        );
        this.defaultStylesheets = List.copyOf(
            checkArgumentNotNull(defaultStylesheets, "Default stylesheets")
        );
    }

    @Override
    public List<String> getStylesheetsByViewId(String viewId) {
        return this.viewIdToStylesheetList.getOrDefault(viewId, this.defaultStylesheets);
    }

}
