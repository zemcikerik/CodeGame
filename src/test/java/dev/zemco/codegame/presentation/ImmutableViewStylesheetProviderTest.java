package dev.zemco.codegame.presentation;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag(UNIT_TEST)
class ImmutableViewStylesheetProviderTest {

    @Test
    void constructorShouldThrowIllegalArgumentExceptionIfMappingMapAreNull() {
        assertThrows(IllegalArgumentException.class, () -> new ImmutableViewStylesheetProvider(null));
    }

    @Test
    void getStylesheetsByViewIdShouldThrowIllegalArgumentExceptionIfViewIdIsNull() {
        ImmutableViewStylesheetProvider provider = new ImmutableViewStylesheetProvider(emptyMap());
        assertThrows(IllegalArgumentException.class, () -> provider.getStylesheetsByViewId(null));
    }

    @Test
    void getStylesheetsByViewIdShouldThrowIllegalArgumentExceptionIfViewIdIsEmpty() {
        ImmutableViewStylesheetProvider provider = new ImmutableViewStylesheetProvider(emptyMap());
        assertThrows(IllegalArgumentException.class, () -> provider.getStylesheetsByViewId(""));
    }

    @Test
    void getStylesheetsByViewIdShouldThrowUnknownViewExceptionIfViewIdIsNotIncludedInMappings() {
        ImmutableViewStylesheetProvider provider = new ImmutableViewStylesheetProvider(emptyMap());
        assertThrows(UnknownViewException.class, () -> provider.getStylesheetsByViewId("viewId"));
    }

    @Test
    void getStylesheetsByViewIdShouldReturnStylesheetsByMapping() {
        ImmutableViewStylesheetProvider provider = new ImmutableViewStylesheetProvider(Map.of(
            "viewId", List.of("sheet1", "sheet2")
        ));

        assertThat(provider.getStylesheetsByViewId("viewId"), contains("sheet1", "sheet2"));
    }

}
