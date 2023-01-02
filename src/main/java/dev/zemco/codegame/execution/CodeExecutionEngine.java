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

/**
 * Implementation of the {@link IExecutionEngine execution engine} that executes a {@link Program program}.
 * The {@link Program program} is executed sequentially from the first instruction.
 * <p>
 * This implementation manages its own {@link IExecutionContext execution context} that is retrievable
 * using the {@link #getExecutionContext()} method.
 *
 * @author Erik Zemčík
 */
public class CodeExecutionEngine implements IExecutionEngine {

    private final Program program;
    private final IExecutionContext context;

    // NOTE: position within instruction list, not line position!
    private int position;
    private boolean moveToNextPosition;

    /**
     * Creates an instance of {@link CodeExecutionEngine} that executes a given {@link Program program}.
     * This new instance uses the provided components ({@code memory}, {@code inputSource} and {@code outputSink})
     * as a {@link IExecutionContext execution context}.
     *
     * @param program program to execute
     * @param memory memory to use during execution
     * @param inputSource input source to use during execution
     * @param outputSink output sink to use during execution
     * @throws IllegalArgumentException if any parameter is {@code null}
     */
    public CodeExecutionEngine(Program program, IMemory memory, IInputSource inputSource, IOutputSink outputSink) {
        this.program = checkArgumentNotNull(program, "Program");
        this.context = new ImmutableExecutionContext(this, memory, inputSource, outputSink);

        this.position = 0;
        this.moveToNextPosition = false;
    }

    /**
     * Performs a jump to an instruction that is followed by the specified {@code label}.
     * This label must be known by the {@link Program program}, which is executed by the engine,
     * and must be included in the {@link Map map} accessible using {@link Program#getJumpLabelToLinePositionMap()}.
     *
     * @param label label to jump to
     * @throws IllegalArgumentException if {@code label} is {@code null} or empty
     * @throws UnknownJumpLabelException if {@code label} is not known by the {@link Program program}
     */
    @Override
    public void jumpToLabel(String label) {
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

    /**
     * Returns a descriptor of the instruction that will be executed next by the engine.
     * This method returns an empty {@link Optional} if the engine has reached the end of the execution.
     *
     * @return instruction descriptor of the next instruction or an empty {@link Optional}
     */
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
            throw new NoNextInstructionException("No next instruction to execute!");
        }

        this.moveToNextPosition = true;
        IInstruction instruction = nextDescriptor.get().getInstruction();

        // capture current position before executing the instruction as it could be changed by the instruction
        int capturedPosition = this.position;

        try {
            instruction.execute(this.context);
        } catch (InstructionExecutionException e) {
            throw new StepExecutionException("Failed to execute instruction!", e, capturedPosition);
        }

        if (this.moveToNextPosition) {
            this.position++;
        }
    }

    /**
     * Returns the {@link IExecutionContext execution context} managed by this engine.
     * @return execution context managed by this engine
     */
    public IExecutionContext getExecutionContext() {
        return this.context;
    }

}
