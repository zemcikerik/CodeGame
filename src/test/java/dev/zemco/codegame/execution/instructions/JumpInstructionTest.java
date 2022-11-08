package dev.zemco.codegame.execution.instructions;

import dev.zemco.codegame.execution.ExecutionContext;
import dev.zemco.codegame.execution.ExecutionEngine;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JumpInstructionTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowIfLabelIsNull() {
        new JumpInstruction(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowIfLabelIsEmpty() {
        new JumpInstruction("");
    }

    @Test
    public void executeShouldRequestExecutionEngineToJumpToLabel() throws InstructionExecutionException {
        ExecutionContext executionContext = mock(ExecutionContext.class);
        ExecutionEngine engine = mock(ExecutionEngine.class);

        when(executionContext.getExecutionEngine()).thenReturn(engine);
        doNothing().when(engine).jumpTo(anyString());

        new JumpInstruction("test").execute(executionContext);

        verify(engine, times(1)).jumpTo("test");
    }

}
