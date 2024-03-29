package dev.zemco.codegame.presentation;

import dev.zemco.codegame.resources.ResourceLoadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag(UNIT_TEST)
class ResourceFxmlViewSourceProviderTest {

    private Class<?> resourceClass;

    @BeforeEach
    public void setUp() {
        this.resourceClass = ResourceFxmlViewSourceProviderTest.class;
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionIfResourceClassIsNull() {
        Map<String, String> resourcePathMap = emptyMap();
        assertThrows(IllegalArgumentException.class, () -> new ResourceFxmlViewSourceProvider(null, resourcePathMap));
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionIfMappingsMapIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ResourceFxmlViewSourceProvider(this.resourceClass, null));
    }

    @Test
    void getFxmlViewSourceStreamByIdShouldThrowIllegalArgumentExceptionIfViewIdIsNull() {
        ResourceFxmlViewSourceProvider provider = new ResourceFxmlViewSourceProvider(this.resourceClass, emptyMap());
        assertThrows(IllegalArgumentException.class, () -> provider.getFxmlViewSourceStreamById(null));
    }

    @Test
    void getFxmlViewSourceStreamByIdShouldThrowIllegalArgumentExceptionIfViewIdIsEmpty() {
        ResourceFxmlViewSourceProvider provider = new ResourceFxmlViewSourceProvider(this.resourceClass, emptyMap());
        assertThrows(IllegalArgumentException.class, () -> provider.getFxmlViewSourceStreamById(""));
    }

    @Test
    void getFxmlViewSourceStreamByIdShouldThrowUnknownViewExceptionIfViewIdIsIncludedInMappings() {
        ResourceFxmlViewSourceProvider provider = new ResourceFxmlViewSourceProvider(this.resourceClass, emptyMap());
        assertThrows(UnknownViewException.class, () -> provider.getFxmlViewSourceStreamById("unknown"));
    }

    @Test
    void getFxmlViewSourceStreamByIdShouldThrowResourceLoadExceptionIfResourceClassResourceIsNotAvailable() {
        ResourceFxmlViewSourceProvider provider = new ResourceFxmlViewSourceProvider(this.resourceClass, Map.of(
            "viewId", "/unknown"
        ));
        assertThrows(ResourceLoadException.class, () -> provider.getFxmlViewSourceStreamById("viewId"));
    }

    @Test
    void getFxmlViewSourceStreamByIdShouldReturnRawFxmlViewSourceStreamForView() {
        ResourceFxmlViewSourceProvider provider = new ResourceFxmlViewSourceProvider(this.resourceClass, Map.of(
            "viewId", "/mock-resource.txt"
        ));
        InputStream result = provider.getFxmlViewSourceStreamById("viewId");
        assertThat(result, notNullValue());
    }

}
