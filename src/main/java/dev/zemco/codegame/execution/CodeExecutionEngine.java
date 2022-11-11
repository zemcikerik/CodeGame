package dev.zemco.codegame.execution;

import dev.zemco.codegame.compilation.IInstructionDescriptor;
import dev.zemco.codegame.compilation.Program;
import dev.zemco.codegame.execution.instructions.IInstruction;
import dev.zemco.codegame.execution.instructions.InstructionExecutionException;
import dev.zemco.codegame.execution.io.IInputSource;
import dev.zemco.codegame.execution.io.IOutputSink;
import dev.zemco.codegame.execution.memory.IMemory;

import java.util.List;
import java.util.Map;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNullAndNotEmpty;

// position differs from line position!
public class CodeExecutionEngine implements IExecutionEngine {

    private final Program program;
    private final IExecutionContext context;
    private int position;
    private boolean moveToNextPosition;

    public CodeExecutionEngine(Program program, IMemory memory, IInputSource inputSource, IOutputSink outputSink) {
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
        List<IInstructionDescriptor> instructionDescriptors = this.program.getInstructionDescriptors();

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
        IInstructionDescriptor descriptor = program.getInstructionDescriptors().get(this.position);
        IInstruction instruction = descriptor.getInstruction();

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
