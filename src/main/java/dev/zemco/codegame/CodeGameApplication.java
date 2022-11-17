package dev.zemco.codegame;

import dev.zemco.codegame.compilation.CodeProgramCompiler;
import dev.zemco.codegame.compilation.IProgramCompiler;
import dev.zemco.codegame.compilation.parsing.FactorySingleIntegerParameterInstructionParser;
import dev.zemco.codegame.compilation.parsing.FactorySingleParameterInstructionParser;
import dev.zemco.codegame.compilation.parsing.IInstructionParser;
import dev.zemco.codegame.compilation.parsing.SupplierInstructionParser;
import dev.zemco.codegame.execution.instructions.AdditionInstruction;
import dev.zemco.codegame.execution.instructions.InputInstruction;
import dev.zemco.codegame.execution.instructions.JumpInstruction;
import dev.zemco.codegame.execution.instructions.OutputInstruction;
import dev.zemco.codegame.presentation.FxmlResourceViewSourceProvider;
import dev.zemco.codegame.presentation.FxmlViewProvider;
import dev.zemco.codegame.presentation.IControllerFactory;
import dev.zemco.codegame.presentation.IViewProvider;
import dev.zemco.codegame.presentation.IViewSourceProvider;
import dev.zemco.codegame.presentation.solution.CodeHighlightStyleComputer;
import dev.zemco.codegame.presentation.solution.IHighlightStyleComputer;
import dev.zemco.codegame.presentation.solution.SolutionController;
import dev.zemco.codegame.presentation.solution.SolutionModel;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class CodeGameApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        List<IInstructionParser> parsers = List.of(
                new SupplierInstructionParser("in", InputInstruction::new),
                new SupplierInstructionParser("out", OutputInstruction::new),
                new FactorySingleIntegerParameterInstructionParser("add", AdditionInstruction::new),
                new FactorySingleParameterInstructionParser("jump", JumpInstruction::new)
        );
        IProgramCompiler compiler = new CodeProgramCompiler(parsers);

        IHighlightStyleComputer highlightStyleComputer = new CodeHighlightStyleComputer(List.of("in", "out", "add", "jump"));
        IViewSourceProvider viewSourceProvider = new FxmlResourceViewSourceProvider();
        IControllerFactory controllerFactory = (ignored) -> new SolutionController(new SolutionModel(compiler), highlightStyleComputer);
        IViewProvider viewProvider = new FxmlViewProvider(viewSourceProvider, controllerFactory);

        primaryStage.setScene(viewProvider.getViewByName("Solution"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
//        IInputSource inputSource = new IterableInputSource(problemCase.getInputs());
//        IOutputSink outputSink = new LoggingOutputSinkDecorator(new VerifyingInputSourceToOutputSinkAdapter(new IterableInputSource(problemCase.getExpectedOutputs())));
//        IMemory memory = new LoggingMemoryDecorator(new ConstantSizeMemory(caseMemorySettings.getSize()));
//        IExecutionEngine engine = new CodeExecutionEngine(program, memory, inputSource, outputSink);
    }

}
