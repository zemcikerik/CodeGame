package dev.zemco.codegame.execution.engine;

import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.instructions.IInstruction;
import dev.zemco.codegame.execution.instructions.InstructionExecutionException;
import dev.zemco.codegame.execution.io.IInputSource;
import dev.zemco.codegame.execution.io.IOutputSink;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.programs.InstructionDescriptor;
import dev.zemco.codegame.programs.Program;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProgramExecutionEngineTest {

    @Mock
    private Program program;

    @Mock
    private IInputSource inputSource;

    @Mock
    private IOutputSink outputSink;

    @Mock
    private IMemory memory;

    @InjectMocks
    private ProgramExecutionEngine engine;

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfAnyArgumentIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProgramExecutionEngine(null, this.memory, this.inputSource, this.outputSink)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProgramExecutionEngine(this.program, null, this.inputSource, this.outputSink)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProgramExecutionEngine(this.program, this.memory, null, this.outputSink)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProgramExecutionEngine(this.program, this.memory, this.inputSource, null)
        );
    }

    @Test
    public void jumpToLabelShouldThrowIllegalArgumentExceptionIfLabelIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.engine.jumpToLabel(null));
    }

    @Test
    public void jumpToLabelShouldThrowIllegalArgumentExceptionIfLabelIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> this.engine.jumpToLabel(""));
    }

    @Test
    public void jumpToLabelShouldThrowUnknownJumpLabelExceptionIfLabelIsNotDefinedInExecutedProgram() {
        when(this.program.getJumpLabelToLinePositionMap()).thenReturn(emptyMap());
        assertThrows(UnknownJumpLabelException.class, () -> this.engine.jumpToLabel("unknown"));
    }

    @Test
    public void jumpToLabelShouldSetNextInstructionToExecuteAfterJumpLabel() {
        InstructionDescriptor instructionToSkip = this.mockInstructionAtLine(0);
        InstructionDescriptor targetInstruction = this.mockInstructionAtLine(2);
        when(this.program.getInstructionDescriptors()).thenReturn(List.of(instructionToSkip, targetInstruction));
        when(this.program.getJumpLabelToLinePositionMap()).thenReturn(Map.of("label", 1));

        this.engine.jumpToLabel("label");

        Optional<InstructionDescriptor> next = this.engine.getNextInstructionDescriptor();
        assertThat(next.isPresent(), is(true));
        assertThat(next.get(), equalTo(targetInstruction));
    }

    @Test
    public void jumpToLabelShouldSetNextInstructionToExecuteAtEndOfProgramIfLabelIsDefinedAfterAllInstructions() {
        when(this.program.getInstructionDescriptors()).thenReturn(List.of(this.mockInstructionAtLine(5)));
        when(this.program.getJumpLabelToLinePositionMap()).thenReturn(Map.of("label", 7));

        this.engine.jumpToLabel("label");

        assertThat(this.engine.getNextInstructionDescriptor().isEmpty(), is(true));
    }

    @Test
    public void stepShouldExecuteInstructionsInOrderAsDefinedByProgram() throws InstructionExecutionException {
        InstructionDescriptor firstInstruction = this.mockInstructionAtLine(0);
        InstructionDescriptor secondInstruction = this.mockInstructionAtLine(1);
        when(this.program.getInstructionDescriptors()).thenReturn(List.of(firstInstruction, secondInstruction));

        this.engine.step();
        verify(firstInstruction.getInstruction(), times(1)).execute(any(IExecutionContext.class));

        this.engine.step();
        verify(secondInstruction.getInstruction(), times(1)).execute(any(IExecutionContext.class));
    }

    @Test
    public void stepShouldThrowStepExecutionExceptionWithContextIfInstructionExecutionHasFailed() throws InstructionExecutionException {
        InstructionDescriptor descriptor = this.mockInstructionAtLine(42);

        doThrow(InstructionExecutionException.class)
            .when(descriptor.getInstruction())
            .execute(any(IExecutionContext.class));

        when(this.program.getInstructionDescriptors()).thenReturn(List.of(descriptor));

        StepExecutionException exception = assertThrows(
            StepExecutionException.class,
            () -> this.engine.step()
        );
        assertThat(exception.getLinePosition(), is(42));
    }

    @Test
    public void stepShouldThrowNoNextInstructionExceptionIfEndOfProgramWasReached() {
        when(this.program.getInstructionDescriptors()).thenReturn(List.of());
        assertThrows(NoNextInstructionException.class, () -> this.engine.step());
    }

    @Test
    public void getExecutionContextReturnsContextWithProvidedExecutionComponents() {
        IExecutionContext result = this.engine.getExecutionContext();

        assertThat(result.getExecutionEngine(), equalTo(this.engine));
        assertThat(result.getInputSource(), equalTo(this.inputSource));
        assertThat(result.getOutputSink(), equalTo(this.outputSink));
        assertThat(result.getMemory(), equalTo(this.memory));
    }

    private InstructionDescriptor mockInstructionAtLine(int linePosition) {
        IInstruction instruction = mock(IInstruction.class);
        return new InstructionDescriptor(instruction, linePosition);
    }

}
