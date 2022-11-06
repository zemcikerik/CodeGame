package dev.zemco.codegame.execution;

import dev.zemco.codegame.compilation.Program;
import dev.zemco.codegame.execution.instructions.Instruction;
import dev.zemco.codegame.execution.instructions.InstructionExecutionException;
import dev.zemco.codegame.execution.io.InputSource;
import dev.zemco.codegame.execution.io.OutputSink;
import dev.zemco.codegame.execution.memory.Memory;

import java.util.List;

// TODO: javadoc
// TODO: builder
public class CodeExecutionEngine implements ExecutionEngine {

    private final Program program;
    private final ExecutionContext context;
    private int position;

    public CodeExecutionEngine(Program program, Memory memory, InputSource inputSource, OutputSink outputSink) {
        this.program = program;
        this.context = new CodeExecutionContext(this, memory, inputSource, outputSink);
        this.position = 0;
    }

    @Override
    public void step() {
        List<Instruction> instructions = program.getInstructions();
        Instruction instruction = instructions.get(this.position);

        try {
            instruction.execute(this.context);
        } catch (InstructionExecutionException e) {
            // TODO: wrap with proper type or ignore?
            throw new RuntimeException(e);
        }

        this.position++;
    }

    // TODO: maybe move me?
    private static class CodeExecutionContext implements ExecutionContext {

        private final ExecutionEngine engine;
        private final Memory memory;
        private final InputSource inputSource;
        private final OutputSink outputSink;

        public CodeExecutionContext(ExecutionEngine engine, Memory memory, InputSource inputSource, OutputSink outputSink) {
            this.engine = engine;
            this.memory = memory;
            this.inputSource = inputSource;
            this.outputSink = outputSink;
        }

        @Override
        public ExecutionEngine getExecutionEngine() {
            return this.engine;
        }

        @Override
        public Memory getMemory() {
            return this.memory;
        }

        @Override
        public InputSource getInputSource() {
            return this.inputSource;
        }

        @Override
        public OutputSink getOutputSink() {
            return this.outputSink;
        }

    }

}
