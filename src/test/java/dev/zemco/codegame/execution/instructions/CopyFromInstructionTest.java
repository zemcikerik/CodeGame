package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
class CopyFromInstructionTest {

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
    void constructorShouldThrowIllegalArgumentExceptionIfSourceCellAddressIsNotPositiveInteger() {
        assertThrows(IllegalArgumentException.class, () -> new CopyFromInstruction(-1));
    }

    @Test
    void executeShouldThrowIllegalArgumentExceptionIfExecutionContextIsNull() {
        CopyFromInstruction instruction = new CopyFromInstruction(1);
        assertThrows(IllegalArgumentException.class, () -> instruction.execute(null));
    }

    @Test
    void executeShouldThrowInstructionExecutionExceptionIfSourceCellAddressIsOutOfMemoryBounds() {
        when(this.memory.getCellByAddress(5)).thenThrow(IndexOutOfBoundsException.class);
        CopyFromInstruction instruction = new CopyFromInstruction(5);
        assertThrows(InstructionExecutionException.class, () -> instruction.execute(this.executionContext));
    }

    @Test
    void executeShouldThrowInstructionExecutionExceptionIfSourceCellHoldsNoValue() {
        when(this.cellAtAddressTwo.hasValue()).thenReturn(false);
        CopyFromInstruction instruction = new CopyFromInstruction(2);
        assertThrows(InstructionExecutionException.class, () -> instruction.execute(this.executionContext));
    }

    @Test
    void executeShouldCopyValueFromSpecifiedCellToWorkingCell() throws InstructionExecutionException {
        when(this.cellAtAddressTwo.hasValue()).thenReturn(true);
        when(this.cellAtAddressTwo.getValue()).thenReturn(42);

        new CopyFromInstruction(2).execute(this.executionContext);

        verify(this.workingCell, times(1)).setValue(42);
    }

}
