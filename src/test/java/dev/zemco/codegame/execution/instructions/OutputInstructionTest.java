package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.io.NotAcceptedException;
import dev.zemco.codegame.execution.io.IOutputSink;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OutputInstructionTest {

    @Mock
    private IExecutionContext executionContext;

    @Mock
    private IOutputSink outputSink;

    @Mock
    private IMemory memory;

    @Mock
    private IMemoryCell workingCell;

    private OutputInstruction outputInstruction;

    @BeforeEach
    public void setUp() {
        this.outputInstruction = new OutputInstruction();
        lenient().when(this.executionContext.getMemory()).thenReturn(this.memory);
        lenient().when(this.executionContext.getOutputSink()).thenReturn(this.outputSink);
        lenient().when(this.memory.getWorkingCell()).thenReturn(this.workingCell);
    }

    @Test
    public void executeShouldOutputValueFromWorkingCellToOutputSink() throws InstructionExecutionException {
        when(this.workingCell.hasValue()).thenReturn(true);
        when(this.workingCell.getValue()).thenReturn(42);
        doNothing().when(this.outputSink).accept(anyInt());

        this.outputInstruction.execute(this.executionContext);

        verify(this.outputSink, times(1)).accept(42);
    }

    @Test
    public void executeShouldThrowInstructionExecutionExceptionIfWorkingCellHasNoValue() {
        when(this.workingCell.hasValue()).thenReturn(false);
        assertThrows(InstructionExecutionException.class, () -> this.outputInstruction.execute(this.executionContext));
    }

    @Test
    public void executeShouldThrowInstructionExecutionExceptionIfOutputSinkRejectedTheValue() {
        when(this.workingCell.hasValue()).thenReturn(true);
        when(this.workingCell.getValue()).thenReturn(-5);
        doThrow(NotAcceptedException.class).when(this.outputSink).accept(anyInt());

        assertThrows(InstructionExecutionException.class, () -> this.outputInstruction.execute(this.executionContext));
    }

}
