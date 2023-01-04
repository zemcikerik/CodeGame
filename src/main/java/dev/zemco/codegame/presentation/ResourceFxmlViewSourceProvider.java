package dev.zemco.codegame.presentation;

import dev.zemco.codegame.resources.ResourceLoadException;

import java.io.InputStream;
import java.util.Map;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Provides the JavaFX FXML sources from resources of the specified class for specified views.
 * This implementation's view mappings are immutable.
 *
 * @author Erik Zemčík
 */
public class ResourceFxmlViewSourceProvider implements IFxmlViewSourceProvider {

    private final Class<?> resourceClass;
    private final Map<String, String> viewIdToResourcePathMap;

    /**
     * Creates an instance of {@link ResourceFxmlViewSourceProvider}. The resources are retrieved from the
     * given resource class. The view mappings cannot be modified after the construction.
     *
     * @param resourceClass class to use as source of resources
     * @param viewIdToFxmlResourcePathMap mappings of view ids to their respective FXML source resources
     *
     * @throws IllegalArgumentException if {@code resourceClass} is {@code null} or
     *                                  if {@code viewIdToFxmlResourcePathMap} is {@code null}
     */
    public ResourceFxmlViewSourceProvider(Class<?> resourceClass, Map<String, String> viewIdToFxmlResourcePathMap) {
        this.resourceClass = checkArgumentNotNull(resourceClass, "Resource class");
        this.viewIdToResourcePathMap = Map.copyOf(
            checkArgumentNotNull(viewIdToFxmlResourcePathMap, "View id to FXML resource path map")
        );
    }

    /**
     * Provides an {@link InputStream input stream} with the raw FXML source of the requested view.
     * <p>
     * The view is identified by the {@code viewId} parameter, and is resolved through the view mappings
     * given during the construction of the provider. The resource streams are retrieved from the resource class
     * using the {@link Class#getResourceAsStream(String)} method.
     *
     * @param viewId identification of the view
     * @return input stream with the raw FXML source
     *
     * @throws IllegalArgumentException if {@code viewId} is {@code null} or empty
     * @throws UnknownViewException when the {@code viewId} is not known by the provider
     * @throws ResourceLoadException if the resource specified by the mappings is not available in the resource class
     *
     * @see Class#getResourceAsStream(String)
     */
    @Override
    public InputStream getFxmlViewSourceStreamById(String viewId) {
        checkArgumentNotEmpty(viewId, "View name");

        if (!this.viewIdToResourcePathMap.containsKey(viewId)) {
            String message = String.format("Resource path for view '%s' was not found!", viewId);
            throw new UnknownViewException(message);
        }

        String resourcePath = this.viewIdToResourcePathMap.get(viewId);
        InputStream viewSource = this.resourceClass.getResourceAsStream(resourcePath);

        if (viewSource == null) {
            throw new ResourceLoadException(String.format(
                "FXML resource for view '%s' at path '%s' was not found!",
                viewId, resourcePath
            ));
        }

        return viewSource;
    }

}
