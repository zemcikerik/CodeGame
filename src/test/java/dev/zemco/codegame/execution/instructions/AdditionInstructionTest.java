package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.ExecutionContext;
import dev.zemco.codegame.execution.memory.Memory;
import dev.zemco.codegame.execution.memory.MemoryCell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdditionInstructionTest {

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private Memory memory;

    @Mock
    private MemoryCell workingCell;

    @Before
    public void setup() {
        when(this.executionContext.getMemory()).thenReturn(this.memory);
        when(this.memory.getWorkingCell()).thenReturn(this.workingCell);
    }

    @Test
    public void executeShouldAddAddendToValueOfWorkingCell() throws InstructionExecutionException {
        when(this.workingCell.hasValue()).thenReturn(true);
        when(this.workingCell.getValue()).thenReturn(-15);
        doNothing().when(this.workingCell).setValue(anyInt());

        new AdditionInstruction(45).execute(this.executionContext);

        verify(this.workingCell, times(1)).setValue(30);
    }

    @Test(expected = InstructionExecutionException.class)
    public void executeShouldThrowInstructionExecutionExceptionIfWorkingCellHoldsNoValue() throws InstructionExecutionException {
        when(this.workingCell.hasValue()).thenReturn(false);
        new AdditionInstruction(1).execute(this.executionContext);
    }

}
