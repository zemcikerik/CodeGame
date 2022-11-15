package dev.zemco.codegame.presentation;

import java.io.InputStream;

public interface IViewSourceProvider {
    InputStream getViewSourceByName(String viewName);
}
