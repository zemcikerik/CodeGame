package dev.zemco.codegame.presentation.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
public class JavaFxDialogServiceTest {

    @Mock
    private IAlertFactory alertFactory;

    @Mock
    private Alert alert;

    @InjectMocks
    private JavaFxDialogService dialogService;

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfAlertFactoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new JavaFxDialogService(null));
    }

    @Test
    public void showErrorDialogShouldThrowIllegalArgumentExceptionIfTitleIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.dialogService.showErrorDialog(null, "message"));
    }

    @Test
    public void showErrorDialogShouldThrowIllegalArgumentExceptionIfMessageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.dialogService.showErrorDialog("title", null));
    }

    @Test
    public void showErrorDialogShouldDisplayErrorAlert() {
        when(this.alertFactory.createAlert(AlertType.ERROR)).thenReturn(this.alert);

        this.dialogService.showErrorDialog("title", "message");

        verify(this.alert, times(1)).setTitle("title");
        verify(this.alert, times(1)).setContentText("message");
        verify(this.alert, times(1)).showAndWait();
    }

    @Test
    public void showInformationDialogShouldThrowIllegalArgumentExceptionIfTitleIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.dialogService.showInformationDialog(null, "message"));
    }

    @Test
    public void showInformationDialogShouldThrowIllegalArgumentExceptionIfMessageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.dialogService.showInformationDialog("title", null));
    }

    @Test
    public void showInformationDialogShouldDisplayErrorAlert() {
        when(this.alertFactory.createAlert(AlertType.INFORMATION)).thenReturn(this.alert);

        this.dialogService.showInformationDialog("title", "message");

        verify(this.alert, times(1)).setTitle("title");
        verify(this.alert, times(1)).setContentText("message");
        verify(this.alert, times(1)).showAndWait();
    }

}
