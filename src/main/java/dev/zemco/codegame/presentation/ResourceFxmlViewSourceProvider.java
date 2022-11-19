package dev.zemco.codegame.presentation;

import java.io.InputStream;
import java.util.Map;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class ResourceFxmlViewSourceProvider implements IFxmlViewSourceProvider {

    private final Class<?> resourceClass;
    private final Map<String, String> viewIdToResourcePath;

    public ResourceFxmlViewSourceProvider(Class<?> resourceClass, Map<String, String> viewIdToResourcePath) {
        this.resourceClass = checkArgumentNotNull(resourceClass, "Resource class");
        this.viewIdToResourcePath = checkArgumentNotNull(viewIdToResourcePath, "View id to resource path");
    }

    @Override
    public InputStream getFxmlViewSourceById(String viewId) {
        checkArgumentNotNull(viewId, "View name");

        if (!this.viewIdToResourcePath.containsKey(viewId)) {
            String message = String.format("Resource path for view '%s' was not found", viewId);
            throw new ViewNotFoundException(message);
        }

        String resourcePath = this.viewIdToResourcePath.get(viewId);
        InputStream viewSource = this.resourceClass.getResourceAsStream(resourcePath);

        if (viewSource == null) {
            String message = String.format("FXML resource for view '%s' at path '%s' was not found!", viewId, resourcePath);
            throw new IllegalStateException(message);
        }

        return viewSource;
    }

}
