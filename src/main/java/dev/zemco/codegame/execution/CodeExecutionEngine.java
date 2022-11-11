package dev.zemco.codegame.execution;

import dev.zemco.codegame.compilation.InstructionDescriptor;
import dev.zemco.codegame.compilation.Program;
import dev.zemco.codegame.execution.instructions.Instruction;
import dev.zemco.codegame.execution.instructions.InstructionExecutionException;
import dev.zemco.codegame.execution.io.InputSource;
import dev.zemco.codegame.execution.io.OutputSink;
import dev.zemco.codegame.execution.memory.Memory;

import java.util.List;
import java.util.Map;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNullAndNotEmpty;

// position differs from line position!
public class CodeExecutionEngine implements ExecutionEngine {

    private final Program program;
    private final ExecutionContext context;
    private int position;
    private boolean moveToNextPosition;

    public CodeExecutionEngine(Program program, Memory memory, InputSource inputSource, OutputSink outputSink) {
        this.program = checkArgumentNotNull(program, "Program");
        this.context = new ImmutableExecutionContext(this, memory, inputSource, outputSink);
        this.position = 0;
        this.moveToNextPosition = false;
    }

    @Override
    public void jumpTo(String label) {
        checkArgumentNotNullAndNotEmpty(label, "Label");

        Map<String, Integer> jumpLabelToPositionMap = this.program.getJumpLabelToPositionMap();

        if (!jumpLabelToPositionMap.containsKey(label)) {
            String message = String.format("Unknown jump label '%s'!", label);
            throw new UnknownJumpLabelException(message);
        }

        int position = jumpLabelToPositionMap.get(label);
        List<InstructionDescriptor> instructionDescriptors = this.program.getInstructionDescriptors();

        for (int i = 0; i < instructionDescriptors.size(); i++) {
            if (instructionDescriptors.get(i).getLinePosition() >= position) {
                this.position = i;
                this.moveToNextPosition = false;
                return;
            }
        }

        // TODO: handle me
        throw new IllegalStateException("Couldn't find valid instruction to jump to!");
    }

    @Override
    public void step() {
        this.moveToNextPosition = true;
        InstructionDescriptor descriptor = program.getInstructionDescriptors().get(this.position);
        Instruction instruction = descriptor.getInstruction();

        try {
            instruction.execute(this.context);
        } catch (InstructionExecutionException e) {
            // TODO: ???
            throw new RuntimeException(e);
        }

        if (this.moveToNextPosition) {
            this.position++;
        }
    }

}
