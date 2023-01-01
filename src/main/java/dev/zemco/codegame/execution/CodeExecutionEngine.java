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
import java.util.Optional;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class CodeExecutionEngine implements IExecutionEngine {

    private final Program program;
    private final IExecutionContext context;

    // NOTE: position within instruction list, not line position!
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
        checkArgumentNotEmpty(label, "Label");

        Map<String, Integer> jumpLabelToLinePositionMap = this.program.getJumpLabelToLinePositionMap();

        if (!jumpLabelToLinePositionMap.containsKey(label)) {
            String message = String.format("Unknown jump label '%s'!", label);
            throw new UnknownJumpLabelException(message);
        }

        this.moveToNextPosition = false;
        int jumpLinePosition = jumpLabelToLinePositionMap.get(label);
        List<IInstructionDescriptor> instructionDescriptors = this.program.getInstructionDescriptors();

        // find first defined instruction after target label
        // NOTE: in future performance of this search could be improved
        //       by using modified binary search if the additional performance will be needed
        for (int i = 0; i < instructionDescriptors.size(); i++) {
            if (instructionDescriptors.get(i).getLinePosition() > jumpLinePosition) {
                this.position = i;
                return;
            }
        }

        // label was defined after all instructions (for example at the end of the program), which is valid
        this.position = instructionDescriptors.size();
    }

    @Override
    public Optional<IInstructionDescriptor> getNextInstructionDescriptor() {
        List<IInstructionDescriptor> instructionDescriptors = this.program.getInstructionDescriptors();

        return this.position < instructionDescriptors.size()
            ? Optional.of(instructionDescriptors.get(this.position))
            : Optional.empty();
    }

    @Override
    public void step() {
        Optional<IInstructionDescriptor> nextDescriptor = this.getNextInstructionDescriptor();

        if (nextDescriptor.isEmpty()) {
            throw new IllegalStateException("No next instruction!");
        }

        this.moveToNextPosition = true;
        IInstruction instruction = nextDescriptor.get().getInstruction();

        // capture current position before executing the instruction as it could be changed by the instruction
        int position = this.position;

        try {
            instruction.execute(this.context);
        } catch (InstructionExecutionException e) {
            throw new StepExecutionException("Failed to execute instruction!", e, position);
        }

        if (this.moveToNextPosition) {
            this.position++;
        }
    }

    public IExecutionContext getExecutionContext() {
        return this.context;
    }

}
