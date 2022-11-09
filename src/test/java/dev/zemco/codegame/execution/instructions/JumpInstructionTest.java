package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.ExecutionContext;
import dev.zemco.codegame.execution.ExecutionEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JumpInstructionTest {

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private ExecutionEngine engine;

    @BeforeEach
    public void setUp() {
        when(this.executionContext.getExecutionEngine()).thenReturn(this.engine);
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
    public void executeShouldRequestExecutionEngineToJumpToLabel() throws InstructionExecutionException {
        doNothing().when(this.engine).jumpTo(anyString());
        new JumpInstruction("test").execute(this.executionContext);
        verify(engine, times(1)).jumpTo("test");
    }

    @Test
    public void executeShouldThrowInstructionExecutionExceptionIfEngineFailsToJumpToLabel() {
        // TODO: adjust exception type
        doThrow(RuntimeException.class).when(this.engine).jumpTo(anyString());
        JumpInstruction instruction = new JumpInstruction("label");
        assertThrows(InstructionExecutionException.class, () -> instruction.execute(this.executionContext));
    }

}
