package dev.zemco.codegame.presentation;

import java.io.InputStream;

public interface IFxmlViewSourceProvider {
    InputStream getFxmlViewSourceById(String viewId);
}
