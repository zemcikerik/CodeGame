package dev.zemco.codegame.presentation;

import java.io.InputStream;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNullAndNotEmpty;

public class FxmlResourceViewSourceProvider implements IViewSourceProvider {

    private static final String VIEW_RESOURCE_PATH_FORMAT = "/fxml/%sView.fxml";

    @Override
    public InputStream getViewSourceByName(String viewName) {
        checkArgumentNotNullAndNotEmpty(viewName, "View name");

        String resourceName = String.format(VIEW_RESOURCE_PATH_FORMAT, viewName);
        InputStream viewSource = FxmlResourceViewSourceProvider.class.getResourceAsStream(resourceName);

        if (viewSource == null) {
            // TODO: proper exception
            String message = String.format("FXML resource for view '%s' was not found!", viewName);
            throw new IllegalStateException(message);
        }

        return viewSource;
    }

}
