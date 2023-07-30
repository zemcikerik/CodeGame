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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
class AdditionInstructionTest {

    @Mock
    private IExecutionContext executionContext;

    @Mock
    private IMemory memory;

    @Mock
    private IMemoryCell workingCell;

    @BeforeEach
    public void setUp() {
        lenient().when(this.executionContext.getMemory()).thenReturn(this.memory);
        lenient().when(this.memory.getWorkingCell()).thenReturn(this.workingCell);
    }

    @Test
    void executeShouldThrowIllegalArgumentExceptionIfExecutionContextIsNull() {
        AdditionInstruction instruction = new AdditionInstruction(42);
        assertThrows(IllegalArgumentException.class, () -> instruction.execute(null));
    }

    @Test
    void executeShouldAddAddendToValueOfWorkingCell() throws InstructionExecutionException {
        when(this.workingCell.hasValue()).thenReturn(true);
        when(this.workingCell.getValue()).thenReturn(-15);
        doNothing().when(this.workingCell).setValue(anyInt());

        new AdditionInstruction(45).execute(this.executionContext);

        verify(this.workingCell, times(1)).setValue(30);
    }

    @Test
    void executeShouldThrowInstructionExecutionExceptionIfWorkingCellHoldsNoValue() {
        when(this.workingCell.hasValue()).thenReturn(false);

        AdditionInstruction instruction = new AdditionInstruction(1);
        assertThrows(InstructionExecutionException.class, () -> instruction.execute(this.executionContext));
    }

}
