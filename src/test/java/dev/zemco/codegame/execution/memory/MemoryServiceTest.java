package dev.zemco.codegame.execution.memory;

import dev.zemco.codegame.problems.ProblemCaseMemorySettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemoryServiceTest {

    @Mock
    private IMemoryFactory memoryFactory;

    @InjectMocks
    private MemoryService memoryService;

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfMemoryFactoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new MemoryService(null));
    }

    @Test
    public void getConfiguredMemoryShouldThrowIllegalArgumentExceptionIfMemorySettingsAreNull() {
        assertThrows(IllegalArgumentException.class, () -> this.memoryService.getConfiguredMemory(null));
    }

    @Test
    public void getConfiguredMemoryShouldReturnMemoryInitializedAsDefinedByMemorySettings() {
        // memory holding 4 cells, cell at address 2 should be initialized to value -42
        ProblemCaseMemorySettings settings = new ProblemCaseMemorySettings(4, Map.of(2, -42));

        IMemory memory = mock(IMemory.class);
        IMemoryCell cellAtAddressTwo = mock(IMemoryCell.class);
        when(memory.getCellByAddress(2)).thenReturn(cellAtAddressTwo);
        when(this.memoryFactory.createMemoryWithSize(4)).thenReturn(memory);

        IMemory result = this.memoryService.getConfiguredMemory(settings);

        assertThat(result, equalTo(memory));
        verify(cellAtAddressTwo, times(1)).setValue(-42);
    }

}
