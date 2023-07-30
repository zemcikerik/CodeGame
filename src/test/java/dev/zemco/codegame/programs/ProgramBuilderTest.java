package dev.zemco.codegame.programs;

import dev.zemco.codegame.execution.instructions.IInstruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@Tag(UNIT_TEST)
class ProgramBuilderTest {

    private ProgramBuilder programBuilder;

    @BeforeEach
    public void setUp() {
        this.programBuilder = new ProgramBuilder();
    }

    @Test
    void addInstructionShouldThrowIllegalArgumentExceptionIfInstructionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.programBuilder.addInstruction(null, 1));
    }

    @Test
    void addInstructionShouldThrowIllegalArgumentExceptionIfLinePositionIsNotPositiveInteger() {
        IInstruction instruction = mock(IInstruction.class);
        assertThrows(IllegalArgumentException.class, () -> this.programBuilder.addInstruction(instruction, -1));
    }

    @Test
    void addInstructionShouldAddInstructionWithCorrectDescriptor() {
        IInstruction instruction = mock(IInstruction.class);

        this.programBuilder.addInstruction(instruction, 1);
        Program program = this.programBuilder.build();

        assertThat(program.getInstructionDescriptors(), hasItem(allOf(
            hasProperty("instruction", equalTo(instruction)),
            hasProperty("linePosition", equalTo(1))
        )));
    }

    @Test
    void addJumpLabelMappingShouldThrowIllegalArgumentExceptionIfLabelIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.programBuilder.addJumpLabelMapping(null, 1));
    }

    @Test
    void addJumpLabelMappingShouldThrowIllegalArgumentExceptionIfLabelIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> this.programBuilder.addJumpLabelMapping("", 1));
    }

    @Test
    void addJumpLabelMappingShouldThrowIllegalArgumentExceptionIfLinePositionIsNotPositiveInteger() {
        assertThrows(IllegalArgumentException.class, () -> this.programBuilder.addJumpLabelMapping("test", -1));
    }

    @Test
    void addJumpLabelMappingShouldAddMapping() {
        this.programBuilder.addJumpLabelMapping("test", 1);
        Program program = this.programBuilder.build();
        assertThat(program.getJumpLabelToLinePositionMap(), hasEntry("test", 1));
    }

    @Test
    void addJumpLabelMappingShouldThrowIllegalStateExceptionWhenDuplicateLabelUsed() {
        this.programBuilder.addJumpLabelMapping("test", 1);
        assertThrows(IllegalStateException.class, () -> this.programBuilder.addJumpLabelMapping("test", 1));
    }

    @Test
    void hasJumpLabelMappingShouldThrowIllegalArgumentExceptionIfLabelIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.programBuilder.hasJumpLabelMapping(null));
    }

    @Test
    void hasJumpLabelMappingShouldThrowIllegalArgumentExceptionIfLabelIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> this.programBuilder.hasJumpLabelMapping(""));
    }

    @Test
    void hasJumpLabelMappingShouldReturnFalseIfNoMappingAdded() {
        assertThat(this.programBuilder.hasJumpLabelMapping("test"), is(false));
    }

    @Test
    void hasJumpLabelMappingShouldReturnTrueIfMappingWasAlreadyAdded() {
        this.programBuilder.addJumpLabelMapping("test", 1);
        assertThat(this.programBuilder.hasJumpLabelMapping("test"), is(true));
    }

}
