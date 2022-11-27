package dev.zemco.codegame;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zemco.codegame.compilation.CodeProgramCompiler;
import dev.zemco.codegame.compilation.IProgramCompiler;
import dev.zemco.codegame.compilation.ImmutableInstructionDescriptor;
import dev.zemco.codegame.compilation.parsing.FactorySingleIntegerParameterInstructionParser;
import dev.zemco.codegame.compilation.parsing.FactorySingleParameterInstructionParser;
import dev.zemco.codegame.compilation.parsing.IInstructionParser;
import dev.zemco.codegame.compilation.parsing.SupplierInstructionParser;
import dev.zemco.codegame.execution.CodeExecutionService;
import dev.zemco.codegame.execution.IExecutionService;
import dev.zemco.codegame.execution.instructions.AdditionInstruction;
import dev.zemco.codegame.execution.instructions.CopyFromInstruction;
import dev.zemco.codegame.execution.instructions.CopyToInstruction;
import dev.zemco.codegame.execution.instructions.InputInstruction;
import dev.zemco.codegame.execution.instructions.JumpInstruction;
import dev.zemco.codegame.execution.instructions.OutputInstruction;
import dev.zemco.codegame.execution.io.IInputSourceFactory;
import dev.zemco.codegame.execution.io.IOutputSinkFactory;
import dev.zemco.codegame.execution.io.IterableInputSource;
import dev.zemco.codegame.execution.io.VerifyingInputSourceToOutputSinkAdapter;
import dev.zemco.codegame.execution.memory.ConstantSizeMemory;
import dev.zemco.codegame.execution.memory.IMemoryService;
import dev.zemco.codegame.execution.memory.MemoryService;
import dev.zemco.codegame.presentation.FxmlViewProvider;
import dev.zemco.codegame.presentation.IControllerFactory;
import dev.zemco.codegame.presentation.IFxmlViewSourceProvider;
import dev.zemco.codegame.presentation.INavigator;
import dev.zemco.codegame.presentation.IStageProvider;
import dev.zemco.codegame.presentation.IViewProvider;
import dev.zemco.codegame.presentation.IViewStylesheetProvider;
import dev.zemco.codegame.presentation.ImmutableDefaultViewStylesheetProvider;
import dev.zemco.codegame.presentation.ImmutableStageProvider;
import dev.zemco.codegame.presentation.Navigator;
import dev.zemco.codegame.presentation.ResourceFxmlViewSourceProvider;
import dev.zemco.codegame.presentation.errors.ImmutableProgramErrorModelFactory;
import dev.zemco.codegame.presentation.problems.IProblemListModel;
import dev.zemco.codegame.presentation.problems.ProblemListController;
import dev.zemco.codegame.presentation.problems.ProblemListModel;
import dev.zemco.codegame.presentation.solution.CodeHighlightStyleComputer;
import dev.zemco.codegame.presentation.solution.IHighlightStyleComputer;
import dev.zemco.codegame.presentation.solution.ISolutionModel;
import dev.zemco.codegame.presentation.solution.SolutionController;
import dev.zemco.codegame.presentation.solution.SolutionModel;
import dev.zemco.codegame.problems.IProblemRepository;
import dev.zemco.codegame.problems.UrlObjectMapperProblemRepository;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;

// global todos (some people have global variables, others have global todos)
// TODO: remove functional interface annotations where applicable
// TODO: check correct usage of terms program and solution in source code
// TODO: fix issues with UI scaling - min/pref/max sizes
// TODO: fix formatting in tests
public class CodeGameApplication extends Application {

    private INavigator navigator;

    // TODO: temporary bootstrapping hell during development while project is still in a state of flux,
    //       this will be later replaced with proper solution - either IoC container or some other alternative
    @Override
    public void start(Stage primaryStage) {
        IMemoryService memoryService = new MemoryService(ConstantSizeMemory::new);
        IInputSourceFactory inputSourceFactory = IterableInputSource::new;
        IOutputSinkFactory outputSinkFactory = (iterable) ->
            new VerifyingInputSourceToOutputSinkAdapter(inputSourceFactory.createInputSourceFromIterable(iterable));

        IExecutionService executionService = new CodeExecutionService(
            memoryService, inputSourceFactory, outputSinkFactory
        );

        List<IInstructionParser> parsers = List.of(
            new SupplierInstructionParser("in", InputInstruction::new),
            new SupplierInstructionParser("out", OutputInstruction::new),
            new FactorySingleIntegerParameterInstructionParser("add", AdditionInstruction::new),
            new FactorySingleParameterInstructionParser("jump", JumpInstruction::new),
            new FactorySingleIntegerParameterInstructionParser("save", CopyToInstruction::new),
            new FactorySingleIntegerParameterInstructionParser("load", CopyFromInstruction::new)
        );
        IProgramCompiler compiler = new CodeProgramCompiler(parsers, ImmutableInstructionDescriptor::new);
        IProblemRepository problemRepository = new UrlObjectMapperProblemRepository(
            CodeGameApplication.class.getResource("/problems.json"), new ObjectMapper()
        );
        IHighlightStyleComputer highlightStyleComputer = new CodeHighlightStyleComputer(
            Set.of("in", "out", "add", "jump", "save", "load")
        );

        IFxmlViewSourceProvider viewSourceProvider = new ResourceFxmlViewSourceProvider(
            CodeGameApplication.class,
            Map.of(
                "problem-list", "/fxml/ProblemListView.fxml",
                "solution", "/fxml/SolutionView.fxml"
            )
        );
        IViewStylesheetProvider viewStylesheetProvider = new ImmutableDefaultViewStylesheetProvider(
            Map.of("solution", List.of("/css/SolutionView.css", "/css/styles.css")),
            List.of("/css/styles.css")
        );
        IStageProvider stageProvider = new ImmutableStageProvider(primaryStage);

        ISolutionModel solutionModel = new SolutionModel(
            new ImmutableProgramErrorModelFactory(), compiler, executionService
        );
        IProblemListModel problemListModel = new ProblemListModel(solutionModel, problemRepository);

        IControllerFactory controllerFactory = (controllerClass) -> {
            if (SolutionController.class.equals(controllerClass)) {
                return new SolutionController(solutionModel, this.navigator, highlightStyleComputer);
            }
            return new ProblemListController(problemListModel, this.navigator);
        };

        IViewProvider viewProvider = new FxmlViewProvider(
            viewSourceProvider, controllerFactory, viewStylesheetProvider
        );
        this.navigator = new Navigator(stageProvider, viewProvider);

        primaryStage.setScene(viewProvider.getViewById("problem-list"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
