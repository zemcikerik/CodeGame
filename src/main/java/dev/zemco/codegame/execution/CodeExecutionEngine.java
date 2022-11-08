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

// position differs from line position!
public class CodeExecutionEngine implements ExecutionEngine {

    private final Program program;
    private final ExecutionContext context;
    private int position;

    public CodeExecutionEngine(Program program, Memory memory, InputSource inputSource, OutputSink outputSink) {
        this.program = checkArgumentNotNull(program, "Program");
        this.context = new ImmutableExecutionContext(this, memory, inputSource, outputSink);
        this.position = 0;
    }

    // TODO: clean me up
    @Override
    public void jumpTo(String label) {
        // TODO: check if empty
        checkArgumentNotNull(label, "Label");

        Map<String, Integer> jumpLabelToPositionMap = this.program.getJumpLabelToPositionMap();

        if (!jumpLabelToPositionMap.containsKey(label)) {
            // TODO: proper exception
            throw new IllegalStateException("Unknown jump label!");
        }

        int position = jumpLabelToPositionMap.get(label);

        // TODO: binary search
        List<InstructionDescriptor> instructionDescriptors = this.program.getInstructionDescriptors();

        for (int i = 0; i < instructionDescriptors.size(); i++) {
            if (instructionDescriptors.get(i).getLinePosition() >= position) {
                this.position = i;
                return;
            }
        }
    }

    @Override
    public void step() {
        InstructionDescriptor descriptor = program.getInstructionDescriptors().get(this.position);
        Instruction instruction = descriptor.getInstruction();

        try {
            instruction.execute(this.context);
        } catch (InstructionExecutionException e) {
            // TODO: ???
            throw new RuntimeException(e);
        }

        this.position++;
    }

}
