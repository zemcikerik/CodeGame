//package dev.zemco.codegame;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import dev.zemco.codegame.compilation.CodeProgramCompiler;
//import dev.zemco.codegame.compilation.Program;
//import dev.zemco.codegame.compilation.IProgramCompiler;
//import dev.zemco.codegame.compilation.parsing.FactorySingleIntegerParameterInstructionParser;
//import dev.zemco.codegame.compilation.parsing.FactorySingleParameterInstructionParser;
//import dev.zemco.codegame.compilation.parsing.IInstructionParser;
//import dev.zemco.codegame.compilation.parsing.SupplierInstructionParser;
//import dev.zemco.codegame.execution.CodeExecutionEngine;
//import dev.zemco.codegame.execution.IExecutionEngine;
//import dev.zemco.codegame.execution.instructions.AdditionInstruction;
//import dev.zemco.codegame.execution.instructions.InputInstruction;
//import dev.zemco.codegame.execution.instructions.JumpInstruction;
//import dev.zemco.codegame.execution.instructions.OutputInstruction;
//import dev.zemco.codegame.execution.io.*;
//import dev.zemco.codegame.execution.memory.ConstantSizeMemory;
//import dev.zemco.codegame.execution.memory.LoggingMemoryDecorator;
//import dev.zemco.codegame.execution.memory.IMemory;
//import dev.zemco.codegame.problems.*;
//
//import java.util.List;
//
//public class CodeGameApplication {
//
//    public static void main(String[] args) throws Exception {
//        IProblemRepository repository = new UrlObjectMapperProblemRepository(CodeGameApplication.class.getResource("/problems.json"), new ObjectMapper());
//        Problem problem = repository.getAllProblems().get(0);
//        ProblemCase problemCase = problem.getCases().get(0);
//        ProblemCaseMemorySettings caseMemorySettings = problemCase.getMemorySettings();
//
//        List<IInstructionParser> parsers = List.of(
//                new SupplierInstructionParser("in", InputInstruction::new),
//                new SupplierInstructionParser("out", OutputInstruction::new),
//                new FactorySingleIntegerParameterInstructionParser("add", AdditionInstruction::new),
//                new FactorySingleParameterInstructionParser("jump", JumpInstruction::new)
//        );
//        IProgramCompiler compiler = new CodeProgramCompiler(parsers);
//
//        String rawProgram = ">loop\n in\n add 3\n out\njump loop";
//        Program program = compiler.compileProgram(rawProgram);
//
//        IInputSource inputSource = new IterableInputSource(problemCase.getInputs());
//        IOutputSink outputSink = new LoggingOutputSinkDecorator(new VerifyingInputSourceToOutputSinkAdapter(new IterableInputSource(problemCase.getExpectedOutputs())));
//        IMemory memory = new LoggingMemoryDecorator(new ConstantSizeMemory(caseMemorySettings.getSize()));
//        IExecutionEngine engine = new CodeExecutionEngine(program, memory, inputSource, outputSink);
//
//        while (true) {
//            engine.step();
//        }
//    }
//
//}

package dev.zemco.codegame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CodeGameApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = FXMLLoader.load(CodeGameApplication.class.getResource("/fxml/SolutionView.fxml"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
