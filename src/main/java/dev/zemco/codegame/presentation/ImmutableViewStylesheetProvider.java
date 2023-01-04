package dev.zemco.codegame.presentation;

import java.util.List;
import java.util.Map;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static java.util.stream.Collectors.toUnmodifiableMap;

/**
 * Immutable provider for stylesheets for JavaFX views.
 * The view to stylesheets mappings are specified during construction.
 *
 * @author Erik Zemčík
 */
public class ImmutableViewStylesheetProvider implements IViewStylesheetProvider {

    private final Map<String, List<String>> viewIdToStylesheetListMap;

    /**
     * Creates an instance of {@link ImmutableStageProvider} with the given view to stylesheet mappings.
     * @param viewIdToStylesheetListMap mappings of view ids to their respective stylesheets
     * @throws IllegalArgumentException if {@code viewIdToStyleSheetListMap} is {@code null}
     */
    public ImmutableViewStylesheetProvider(Map<String, List<String>> viewIdToStylesheetListMap) {
        checkArgumentNotNull(viewIdToStylesheetListMap, "View id to stylesheet list");
        this.viewIdToStylesheetListMap = this.deepImmutableCopy(viewIdToStylesheetListMap);
    }

    @Override
    public List<String> getStylesheetsByViewId(String viewId) {
        checkArgumentNotEmpty(viewId, "View id");

        if (this.viewIdToStylesheetListMap.containsKey(viewId)) {
            return this.viewIdToStylesheetListMap.get(viewId);
        }

        String message = String.format("Stylesheets for view '%s' were not found!", viewId);
        throw new UnknownViewException(message);
    }

    private Map<String, List<String>> deepImmutableCopy(Map<String, List<String>> viewIdToStylesheetListMap) {
        return viewIdToStylesheetListMap.entrySet().stream()
            .collect(toUnmodifiableMap(Map.Entry::getKey, entry -> List.copyOf(entry.getValue())));
    }

}
