package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.engine.IExecutionEngine;
import dev.zemco.codegame.execution.engine.UnknownJumpLabelException;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JumpIfZeroInstructionTest {

    @Mock
    private IExecutionContext executionContext;

    @Mock
    private IExecutionEngine engine;

    @Mock
    private IMemory memory;

    @Mock
    private IMemoryCell workingCell;

    @BeforeEach
    public void setUp() {
        lenient().when(this.executionContext.getExecutionEngine()).thenReturn(this.engine);
        lenient().when(this.executionContext.getMemory()).thenReturn(this.memory);
        lenient().when(this.memory.getWorkingCell()).thenReturn(this.workingCell);
    }

    @Test
    public void constructorShouldThrowIfLabelIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new JumpIfZeroInstruction(null));
    }

    @Test
    public void constructorShouldThrowIfLabelIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new JumpIfZeroInstruction(""));
    }

    @Test
    public void executeShouldThrowIllegalArgumentExceptionIfExecutionContextIsNull() {
        JumpIfZeroInstruction instruction = new JumpIfZeroInstruction("label");
        assertThrows(IllegalArgumentException.class, () -> instruction.execute(null));
    }

    @Test
    public void executeShouldThrowInstructionExecutionExceptionIfWorkingCellHoldsNoValue() {
        when(this.workingCell.hasValue()).thenReturn(false);
        JumpIfZeroInstruction instruction = new JumpIfZeroInstruction("label");
        assertThrows(InstructionExecutionException.class, () -> instruction.execute(this.executionContext));
    }

    @Test
    public void executeShouldNotRequestEngineToJumpWhenValueInWorkingCellIsNotZero() throws InstructionExecutionException {
        this.setUpWorkingCellValue(-80);
        new JumpIfZeroInstruction("label").execute(this.executionContext);
        verify(this.engine, never()).jumpToLabel(anyString());
    }

    @Test
    public void executeShouldRequestEngineToJumpWhenValueInWorkingCellIsZero() throws InstructionExecutionException {
        this.setUpWorkingCellValue(0);
        new JumpIfZeroInstruction("label").execute(this.executionContext);
        verify(this.engine, times(1)).jumpToLabel("label");
    }

    @Test
    public void executeShouldThrowInstructionExecutionExceptionIfEngineFailsToJumpToLabel() {
        this.setUpWorkingCellValue(0);
        doThrow(UnknownJumpLabelException.class).when(this.engine).jumpToLabel("label");
        JumpIfZeroInstruction instruction = new JumpIfZeroInstruction("label");

        assertThrows(InstructionExecutionException.class, () -> instruction.execute(this.executionContext));
    }

    private void setUpWorkingCellValue(int value) {
        when(this.workingCell.hasValue()).thenReturn(true);
        when(this.workingCell.getValue()).thenReturn(value);
    }

}
