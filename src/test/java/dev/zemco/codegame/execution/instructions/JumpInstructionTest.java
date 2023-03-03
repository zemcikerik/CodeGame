package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.engine.IExecutionEngine;
import dev.zemco.codegame.execution.engine.UnknownJumpLabelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
public class JumpInstructionTest {

    @Mock
    private IExecutionContext executionContext;

    @Mock
    private IExecutionEngine engine;

    @BeforeEach
    public void setUp() {
        lenient().when(this.executionContext.getExecutionEngine()).thenReturn(this.engine);
    }

    @Test
    public void constructorShouldThrowIfLabelIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new JumpInstruction(null));
    }

    @Test
    public void constructorShouldThrowIfLabelIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new JumpInstruction(""));
    }

    @Test
    public void executeShouldThrowIllegalArgumentExceptionIfExecutionContextIsNull() {
        JumpInstruction instruction = new JumpInstruction("test");
        assertThrows(IllegalArgumentException.class, () -> instruction.execute(null));
    }

    @Test
    public void executeShouldRequestEngineToJumpToLabel() throws InstructionExecutionException {
        doNothing().when(this.engine).jumpToLabel(anyString());
        new JumpInstruction("test").execute(this.executionContext);
        verify(this.engine, times(1)).jumpToLabel("test");
    }

    @Test
    public void executeShouldThrowInstructionExecutionExceptionIfEngineFailsToJumpToLabel() {
        doThrow(UnknownJumpLabelException.class).when(this.engine).jumpToLabel(anyString());
        JumpInstruction instruction = new JumpInstruction("label");
        assertThrows(InstructionExecutionException.class, () -> instruction.execute(this.executionContext));
    }

}
