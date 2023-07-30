package dev.zemco.codegame.presentation;

import dev.zemco.codegame.resources.ResourceLoadException;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
class FxmlViewProviderTest {

    @Mock
    private IFxmlViewSourceProvider viewSourceProvider;

    @Mock
    private IControllerFactory controllerFactory;

    @Mock
    private IViewStylesheetProvider viewStylesheetProvider;

    @Mock
    private IFxmlLoaderFactory fxmlLoaderFactory;

    @Mock
    private FXMLLoader fxmlLoader;

    @InjectMocks
    private FxmlViewProvider fxmlViewProvider;

    @Captor
    private ArgumentCaptor<Callback<Class<?>, Object>> controllerFactoryAdapterCaptor;

    @BeforeEach
    public void setUp() {
        lenient().when(this.fxmlLoaderFactory.createFxmlLoader()).thenReturn(this.fxmlLoader);
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionIfAnyArgumentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FxmlViewProvider(null, this.controllerFactory, this.viewStylesheetProvider, this.fxmlLoaderFactory);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new FxmlViewProvider(this.viewSourceProvider, null, this.viewStylesheetProvider, this.fxmlLoaderFactory);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new FxmlViewProvider(this.viewSourceProvider, this.controllerFactory, null, this.fxmlLoaderFactory);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new FxmlViewProvider(this.viewSourceProvider, this.controllerFactory, this.viewStylesheetProvider, null);
        });
    }

    @Test
    void getViewByIdShouldThrowIllegalArgumentExceptionIfViewIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.fxmlViewProvider.getViewById(null));
    }

    @Test
    void getViewByIdShouldThrowIllegalArgumentExceptionIfViewIdIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> this.fxmlViewProvider.getViewById(""));
    }

    @Test
    void getViewByIdShouldReturnRootNodeOfLoadedView() throws IOException {
        Parent root = this.createMockRootElement();
        InputStream fxmlInputSource = mock(InputStream.class);
        when(this.viewSourceProvider.getFxmlViewSourceStreamById("viewId")).thenReturn(fxmlInputSource);
        when(this.fxmlLoader.load(fxmlInputSource)).thenReturn(root);

        Parent result = this.fxmlViewProvider.getViewById("viewId");

        assertThat(result, equalTo(root));
        verify(this.fxmlLoader, times(1)).load(fxmlInputSource);
    }

    @Test
    void getViewByIdShouldLoadViewsWithControllerFactory() throws IOException {
        Parent root = this.createMockRootElement();
        when(this.viewSourceProvider.getFxmlViewSourceStreamById("viewId")).thenReturn(mock(InputStream.class));
        when(this.fxmlLoader.load(any(InputStream.class))).thenReturn(root);

        this.fxmlViewProvider.getViewById("viewId");

        verify(this.fxmlLoader, times(1)).setControllerFactory(this.controllerFactoryAdapterCaptor.capture());

        this.controllerFactoryAdapterCaptor.getValue().call(FxmlViewProviderTest.class);
        verify(this.controllerFactory, times(1)).createController(FxmlViewProviderTest.class);
    }

    @Test
    void getViewByIdShouldAddStylesheetsToViewRoots() throws IOException {
        Parent root = this.createMockRootElement();
        when(this.viewSourceProvider.getFxmlViewSourceStreamById("viewId")).thenReturn(mock(InputStream.class));
        when(this.viewStylesheetProvider.getStylesheetsByViewId("viewId")).thenReturn(List.of("sheet1", "sheet2"));
        when(this.fxmlLoader.load(any(InputStream.class))).thenReturn(root);

        this.fxmlViewProvider.getViewById("viewId");

        assertThat(root.getStylesheets(), contains("sheet1", "sheet2"));
    }

    @Test
    void getViewByIdShouldThrowResourceLoadExceptionIfFxmlLoaderFailsToLoadRootNode() throws IOException {
        when(this.viewSourceProvider.getFxmlViewSourceStreamById("viewId")).thenReturn(mock(InputStream.class));
        when(this.fxmlLoader.load(any(InputStream.class))).thenThrow(IOException.class);

        assertThrows(ResourceLoadException.class, () -> this.fxmlViewProvider.getViewById("viewId"));
    }

    private Parent createMockRootElement() {
        Parent root = mock(Parent.class);
        when(root.getStylesheets()).thenReturn(FXCollections.observableArrayList());
        return root;
    }

}
