package dev.zemco.codegame.presentation;

import javafx.stage.Stage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@Tag(UNIT_TEST)
class ImmutableStageProviderTest {

    @Test
    void constructorShouldThrowIllegalArgumentExceptionIfPrimaryStageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ImmutableStageProvider(null));
    }

    @Test
    void getPrimaryStageShouldReturnPrimaryStage() {
        Stage primaryStage = mock(Stage.class);
        ImmutableStageProvider provider = new ImmutableStageProvider(primaryStage);
        assertThat(provider.getPrimaryStage(), is(primaryStage));
    }

}
