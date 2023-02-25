package dev.zemco.codegame.presentation;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class ImmutableStageProviderTest {

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfPrimaryStageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ImmutableStageProvider(null));
    }

    @Test
    public void getPrimaryStageShouldReturnPrimaryStage() {
        Stage primaryStage = mock(Stage.class);
        ImmutableStageProvider provider = new ImmutableStageProvider(primaryStage);
        assertThat(provider.getPrimaryStage(), is(primaryStage));
    }

}
