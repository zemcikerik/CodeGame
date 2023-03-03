package dev.zemco.codegame.presentation;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
public class SimpleViewIdNavigatorTest {

    @Mock
    private IStageProvider stageProvider;

    @Mock
    private IViewProvider viewProvider;

    @Mock
    private Stage primaryStage;

    @Mock
    private Scene primaryStageScene;

    @InjectMocks
    private SimpleViewIdNavigator navigator;

    @BeforeEach
    public void setUp() {
        lenient().when(this.stageProvider.getPrimaryStage()).thenReturn(this.primaryStage);
        lenient().when(this.primaryStage.getScene()).thenReturn(this.primaryStageScene);
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionWhenStageProviderIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleViewIdNavigator(null, this.viewProvider));
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionWhenViewProviderIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleViewIdNavigator(this.stageProvider, null));
    }

    @Test
    public void navigateToShouldThrowIllegalArgumentExceptionIfViewIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.navigator.navigateTo(null));
    }

    @Test
    public void navigateToShouldThrowIllegalArgumentExceptionIfViewIdIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> this.navigator.navigateTo(""));
    }

    @Test
    public void navigateToShouldSetSceneRootToViewByViewId() {
        Parent root = mock(Parent.class);
        when(this.viewProvider.getViewById("viewId")).thenReturn(root);

        this.navigator.navigateTo("viewId");

        verify(this.primaryStageScene, times(1)).setRoot(root);
    }

    @Test
    public void navigateToShouldThrowUnknownRouteExceptionIfViewIdIsNotKnownByViewProvider() {
        when(this.viewProvider.getViewById("unknown")).thenThrow(UnknownViewException.class);
        assertThrows(UnknownRouteException.class, () -> this.navigator.navigateTo("unknown"));
    }

}
