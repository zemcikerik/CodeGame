package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.io.IInputSource;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InputInstructionTest {

    @Mock
    private IExecutionContext executionContext;

    @Mock
    private IInputSource inputSource;

    @Mock
    private IMemory memory;

    @Mock
    private IMemoryCell workingCell;

    private InputInstruction inputInstruction;

    @BeforeEach
    public void setUp() {
        this.inputInstruction = new InputInstruction();
        lenient().when(this.executionContext.getInputSource()).thenReturn(this.inputSource);
        lenient().when(this.executionContext.getMemory()).thenReturn(this.memory);
        lenient().when(this.memory.getWorkingCell()).thenReturn(this.workingCell);
    }

    @Test
    public void executeShouldSetWorkingCellValueToNextValueFromInputSource() throws InstructionExecutionException {
        when(this.inputSource.hasNextValue()).thenReturn(true);
        when(this.inputSource.getNextValue()).thenReturn(42);
        doNothing().when(this.workingCell).setValue(anyInt());

        this.inputInstruction.execute(this.executionContext);

        verify(this.workingCell, times(1)).setValue(42);
    }

    @Test
    public void executeShouldThrowInstructionExecutionExceptionIfInputSourceHasNoMoreValues() {
        when(this.inputSource.hasNextValue()).thenReturn(false);
        assertThrows(InstructionExecutionException.class, () -> this.inputInstruction.execute(this.executionContext));
    }

}
