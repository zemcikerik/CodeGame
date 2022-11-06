package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.ExecutionContext;
import dev.zemco.codegame.execution.io.NotAcceptedException;
import dev.zemco.codegame.execution.io.OutputSink;
import dev.zemco.codegame.execution.memory.Memory;
import dev.zemco.codegame.execution.memory.MemoryCell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OutputInstructionTest {

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private OutputSink outputSink;

    @Mock
    private Memory memory;

    @Mock
    private MemoryCell workingCell;

    private OutputInstruction outputInstruction;

    @Before
    public void setup() {
        this.outputInstruction = new OutputInstruction();
        when(this.executionContext.getMemory()).thenReturn(this.memory);
        when(this.executionContext.getOutputSink()).thenReturn(this.outputSink);
        when(this.memory.getWorkingCell()).thenReturn(this.workingCell);
    }

    @Test
    public void executeShouldOutputValueFromWorkingCellToOutputSink() throws InstructionExecutionException {
        when(this.workingCell.hasValue()).thenReturn(true);
        when(this.workingCell.getValue()).thenReturn(42);
        doNothing().when(this.outputSink).accept(anyInt());

        this.outputInstruction.execute(this.executionContext);

        verify(this.outputSink, times(1)).accept(42);
    }

    @Test(expected = InstructionExecutionException.class)
    public void executeShouldThrowInstructionExecutionExceptionIfWorkingCellHasNoValue() throws InstructionExecutionException {
        when(this.workingCell.hasValue()).thenReturn(false);
        this.outputInstruction.execute(this.executionContext);
    }

    @Test(expected = InstructionExecutionException.class)
    public void executeShouldThrowInstructionExecutionExceptionIfOutputSinkRejectedTheValue() throws InstructionExecutionException {
        when(this.workingCell.hasValue()).thenReturn(true);
        when(this.workingCell.getValue()).thenReturn(-5);
        doThrow(NotAcceptedException.class).when(this.outputSink).accept(anyInt());

        this.outputInstruction.execute(this.executionContext);
    }

}
