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
class JavaFxDialogServiceTest {

    @Mock
    private IAlertFactory alertFactory;

    @Mock
    private Alert alert;

    @InjectMocks
    private JavaFxDialogService dialogService;

    @Test
    void constructorShouldThrowIllegalArgumentExceptionIfAlertFactoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new JavaFxDialogService(null));
    }

    @Test
    void showErrorDialogShouldThrowIllegalArgumentExceptionIfTitleIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.dialogService.showErrorDialog(null, "message"));
    }

    @Test
    void showErrorDialogShouldThrowIllegalArgumentExceptionIfMessageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.dialogService.showErrorDialog("title", null));
    }

    @Test
    void showErrorDialogShouldDisplayErrorAlert() {
        when(this.alertFactory.createAlert(AlertType.ERROR)).thenReturn(this.alert);

        this.dialogService.showErrorDialog("title", "message");

        verify(this.alert, times(1)).setTitle("title");
        verify(this.alert, times(1)).setContentText("message");
        verify(this.alert, times(1)).showAndWait();
    }

    @Test
    void showInformationDialogShouldThrowIllegalArgumentExceptionIfTitleIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.dialogService.showInformationDialog(null, "message"));
    }

    @Test
    void showInformationDialogShouldThrowIllegalArgumentExceptionIfMessageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.dialogService.showInformationDialog("title", null));
    }

    @Test
    void showInformationDialogShouldDisplayErrorAlert() {
        when(this.alertFactory.createAlert(AlertType.INFORMATION)).thenReturn(this.alert);

        this.dialogService.showInformationDialog("title", "message");

        verify(this.alert, times(1)).setTitle("title");
        verify(this.alert, times(1)).setContentText("message");
        verify(this.alert, times(1)).showAndWait();
    }

}
