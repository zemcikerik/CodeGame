package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.ExecutionContext;
import dev.zemco.codegame.execution.io.InputSource;
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
public class InputInstructionTest {

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private InputSource inputSource;

    @Mock
    private Memory memory;

    @Mock
    private MemoryCell workingCell;

    private InputInstruction inputInstruction;

    @Before
    public void setup() {
        this.inputInstruction = new InputInstruction();
        when(this.executionContext.getInputSource()).thenReturn(this.inputSource);
        when(this.executionContext.getMemory()).thenReturn(this.memory);
        when(this.memory.getWorkingCell()).thenReturn(this.workingCell);
    }

    @Test
    public void executeShouldSetWorkingCellValueToNextValueFromInputSource() throws InstructionExecutionException {
        when(this.inputSource.hasNextValue()).thenReturn(true);
        when(this.inputSource.getNextValue()).thenReturn(42);
        doNothing().when(this.workingCell).setValue(anyInt());

        this.inputInstruction.execute(this.executionContext);

        verify(this.workingCell, times(1)).setValue(42);
    }

    @Test(expected = InstructionExecutionException.class)
    public void executeShouldThrowInstructionExecutionExceptionIfInputSourceHasNoMoreValues() throws InstructionExecutionException {
        when(this.inputSource.hasNextValue()).thenReturn(false);
        this.inputInstruction.execute(this.executionContext);
    }

}
