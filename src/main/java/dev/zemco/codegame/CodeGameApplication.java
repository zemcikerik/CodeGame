package dev.zemco.codegame;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zemco.codegame.compilation.CodeProgramCompiler;
import dev.zemco.codegame.compilation.Program;
import dev.zemco.codegame.compilation.ProgramCompiler;
import dev.zemco.codegame.compilation.parsing.AdditionInstructionParser;
import dev.zemco.codegame.compilation.parsing.FactorySingleParameterInstructionParser;
import dev.zemco.codegame.compilation.parsing.InstructionParser;
import dev.zemco.codegame.compilation.parsing.SupplierInstructionParser;
import dev.zemco.codegame.execution.CodeExecutionEngine;
import dev.zemco.codegame.execution.ExecutionEngine;
import dev.zemco.codegame.execution.instructions.InputInstruction;
import dev.zemco.codegame.execution.instructions.JumpInstruction;
import dev.zemco.codegame.execution.instructions.OutputInstruction;
import dev.zemco.codegame.execution.io.InputSource;
import dev.zemco.codegame.execution.io.IterableInputSource;
import dev.zemco.codegame.execution.io.NoOpOutputSink;
import dev.zemco.codegame.execution.io.OutputSink;
import dev.zemco.codegame.execution.memory.ConstantSizeMemory;
import dev.zemco.codegame.execution.memory.LoggingMemoryDecorator;
import dev.zemco.codegame.execution.memory.Memory;
import dev.zemco.codegame.problems.Problem;
import dev.zemco.codegame.problems.ProblemRepository;
import dev.zemco.codegame.problems.UrlObjectMapperProblemRepository;

import java.util.List;

public class CodeGameApplication {

    public static void main(String[] args) throws Exception {
        ProblemRepository repository = new UrlObjectMapperProblemRepository(CodeGameApplication.class.getResource("/problems.json"), new ObjectMapper());
        Problem problem = repository.getAllProblems().get(0);

        List<InstructionParser> parsers = List.of(
                new SupplierInstructionParser("in", InputInstruction::new),
                new SupplierInstructionParser("out", OutputInstruction::new),
                new AdditionInstructionParser(),
                new FactorySingleParameterInstructionParser("jump", JumpInstruction::new)
        );
        ProgramCompiler compiler = new CodeProgramCompiler(parsers);

        String rawProgram = ">test\nin\n add 5\n out\njump test";
        Program program = compiler.compileProgram(rawProgram);

        InputSource inputSource = new IterableInputSource(List.of(1, 2, 3));
        OutputSink outputSink = new NoOpOutputSink();
        Memory memory = new LoggingMemoryDecorator(new ConstantSizeMemory(8));
        ExecutionEngine engine = new CodeExecutionEngine(program, memory, inputSource, outputSink);

        while (true) {
            engine.step();
        }
    }

}
