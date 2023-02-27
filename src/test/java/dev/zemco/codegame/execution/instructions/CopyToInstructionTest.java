package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CopyToInstructionTest {

    @Mock
    private IExecutionContext executionContext;

    @Mock
    private IMemory memory;

    @Mock
    private IMemoryCell workingCell;

    @Mock
    private IMemoryCell cellAtAddressTwo;

    @BeforeEach
    public void setUp() {
        lenient().when(this.executionContext.getMemory()).thenReturn(this.memory);
        lenient().when(this.memory.getWorkingCell()).thenReturn(this.workingCell);
        lenient().when(this.memory.getCellByAddress(2)).thenReturn(this.cellAtAddressTwo);
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfDestinationCellAddressIsNotPositiveInteger() {
        assertThrows(IllegalArgumentException.class, () -> new CopyToInstruction(-1));
    }

    @Test
    public void executeShouldThrowIllegalArgumentExceptionIfExecutionContextIsNull() {
        CopyToInstruction instruction = new CopyToInstruction(1);
        assertThrows(IllegalArgumentException.class, () -> instruction.execute(null));
    }

    @Test
    public void executeShouldThrowInstructionExecutionExceptionIfDestinationCellAddressIsOutOfMemoryBounds() {
        when(this.workingCell.hasValue()).thenReturn(true);
        when(this.memory.getCellByAddress(5)).thenThrow(IndexOutOfBoundsException.class);
        CopyToInstruction instruction = new CopyToInstruction(5);

        assertThrows(InstructionExecutionException.class, () -> instruction.execute(this.executionContext));
    }

    @Test
    public void executeShouldThrowInstructionExecutionExceptionIfWorkingCellHoldsNoValue() {
        when(this.workingCell.hasValue()).thenReturn(false);
        CopyToInstruction instruction = new CopyToInstruction(2);
        assertThrows(InstructionExecutionException.class, () -> instruction.execute(this.executionContext));
    }

    @Test
    public void executeShouldCopyValueFromWorkingCellToDestinationCell() throws InstructionExecutionException {
        when(this.workingCell.hasValue()).thenReturn(true);
        when(this.workingCell.getValue()).thenReturn(42);

        new CopyToInstruction(2).execute(this.executionContext);

        verify(this.cellAtAddressTwo, times(1)).setValue(42);
    }

}
