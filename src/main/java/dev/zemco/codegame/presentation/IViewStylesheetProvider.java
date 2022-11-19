package dev.zemco.codegame.presentation;

import java.util.List;

public interface IViewStylesheetProvider {
    List<String> getStylesheetsByViewId(String viewId);
}
