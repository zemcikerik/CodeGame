package dev.zemco.codegame;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zemco.codegame.compilation.CodeProgramCompiler;
import dev.zemco.codegame.compilation.IProgramCompiler;
import dev.zemco.codegame.compilation.parsing.*;
import dev.zemco.codegame.evaluation.OutputSinkSatisfiedEvaluationStrategy;
import dev.zemco.codegame.evaluation.EvaluationService;
import dev.zemco.codegame.evaluation.IEvaluationService;
import dev.zemco.codegame.evaluation.IEvaluationStrategy;
import dev.zemco.codegame.execution.ProgramExecutionService;
import dev.zemco.codegame.execution.IExecutionService;
import dev.zemco.codegame.execution.instructions.*;
import dev.zemco.codegame.execution.io.IInputSourceFactory;
import dev.zemco.codegame.execution.io.IOutputSinkFactory;
import dev.zemco.codegame.execution.io.IterableInputSource;
import dev.zemco.codegame.execution.io.VerifyingInputSourceToOutputSinkAdapter;
import dev.zemco.codegame.execution.memory.ConstantSizeMemory;
import dev.zemco.codegame.execution.memory.IMemoryService;
import dev.zemco.codegame.execution.memory.MemoryService;
import dev.zemco.codegame.execution.memory.SimpleMemoryCell;
import dev.zemco.codegame.presentation.FxmlViewProvider;
import dev.zemco.codegame.presentation.IControllerFactory;
import dev.zemco.codegame.presentation.IFxmlViewSourceProvider;
import dev.zemco.codegame.presentation.INavigator;
import dev.zemco.codegame.presentation.IStageProvider;
import dev.zemco.codegame.presentation.IViewProvider;
import dev.zemco.codegame.presentation.IViewStylesheetProvider;
import dev.zemco.codegame.presentation.ImmutableViewStylesheetProvider;
import dev.zemco.codegame.presentation.ImmutableStageProvider;
import dev.zemco.codegame.presentation.SimpleViewIdNavigator;
import dev.zemco.codegame.presentation.ResourceFxmlViewSourceProvider;
import dev.zemco.codegame.presentation.dialog.IDialogService;
import dev.zemco.codegame.presentation.dialog.JavaFxDialogService;
import dev.zemco.codegame.presentation.solution.errors.SimpleSolutionErrorModelFactory;
import dev.zemco.codegame.presentation.problems.IProblemListModel;
import dev.zemco.codegame.presentation.problems.ProblemListController;
import dev.zemco.codegame.presentation.problems.ProblemListModel;
import dev.zemco.codegame.presentation.highlighting.CodeHighlightStyleComputer;
import dev.zemco.codegame.presentation.highlighting.IHighlightStyleComputer;
import dev.zemco.codegame.presentation.solution.ISolutionModel;
import dev.zemco.codegame.presentation.solution.SolutionController;
import dev.zemco.codegame.presentation.solution.SolutionModel;
import dev.zemco.codegame.problems.IProblemRepository;
import dev.zemco.codegame.problems.UrlObjectMapperProblemRepository;
import dev.zemco.codegame.programs.ProgramBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;

// global todos (some people have global variables, others have global todos)
// TODO: fix issues with UI scaling - min/pref/max sizes
// TODO: fix formatting in tests
public class CodeGameApplication extends Application {

    private INavigator navigator;

    // TODO: temporary bootstrapping hell during development while project is still in a state of flux,
    //       this will be later replaced with proper solution - either IoC container or some other alternative
    @Override
    public void start(Stage primaryStage) {
        IMemoryService memoryService = new MemoryService(memorySize -> new ConstantSizeMemory(memorySize, SimpleMemoryCell::new));
        IInputSourceFactory inputSourceFactory = IterableInputSource::new;
        IOutputSinkFactory outputSinkFactory = (iterable) ->
            new VerifyingInputSourceToOutputSinkAdapter(inputSourceFactory.createInputSourceFromIterable(iterable));

        IExecutionService executionService = new ProgramExecutionService(
            memoryService, inputSourceFactory, outputSinkFactory
        );

        IEvaluationStrategy evaluationStrategy = new OutputSinkSatisfiedEvaluationStrategy();
        IEvaluationService evaluationService = new EvaluationService(executionService, evaluationStrategy);

        IInstructionParser parser = new DelegatingInstructionParser(List.of(
            new SupplierInstructionParser("in", InputInstruction::new),
            new SupplierInstructionParser("out", OutputInstruction::new),
            new FactorySingleIntegerParameterInstructionParser("add", AdditionInstruction::new),
            new FactorySingleParameterInstructionParser("jump", JumpInstruction::new),
            new FactorySingleParameterInstructionParser("jumpzero", JumpIfZeroInstruction::new),
            new FactorySingleIntegerParameterInstructionParser("save", CopyToInstruction::new),
            new FactorySingleIntegerParameterInstructionParser("load", CopyFromInstruction::new)
        ));
        IProgramCompiler compiler = new CodeProgramCompiler(parser, ProgramBuilder::new);

        IProblemRepository problemRepository = new UrlObjectMapperProblemRepository(
            CodeGameApplication.class.getResource("/problems.json"), new ObjectMapper()
        );
        IHighlightStyleComputer highlightStyleComputer = new CodeHighlightStyleComputer(
            Set.of("in", "out", "add", "jump", "jumpzero", "save", "load")
        );

        IDialogService dialogService = new JavaFxDialogService();

        IFxmlViewSourceProvider viewSourceProvider = new ResourceFxmlViewSourceProvider(
            CodeGameApplication.class,
            Map.of(
                "problem-list", "/fxml/ProblemListView.fxml",
                "solution", "/fxml/SolutionView.fxml"
            )
        );
        IViewStylesheetProvider viewStylesheetProvider = new ImmutableViewStylesheetProvider(Map.of(
            "problem-list", List.of(),
            "solution", List.of("/css/SolutionView.css")
        ));
        IStageProvider stageProvider = new ImmutableStageProvider(primaryStage);

        IProblemListModel problemListModel = new ProblemListModel(problemRepository);

        ISolutionModel solutionModel = new SolutionModel(
            problemListModel, compiler, evaluationService,
            new SimpleSolutionErrorModelFactory()
        );

        // TODO: do this properly based on the contract
        IControllerFactory controllerProvider = (controllerClass) -> {
            if (SolutionController.class.equals(controllerClass)) {
                return new SolutionController(solutionModel, this.navigator, dialogService, highlightStyleComputer);
            } else {
                return new ProblemListController(problemListModel, this.navigator);
            }
        };

        IViewProvider viewProvider = new FxmlViewProvider(
            viewSourceProvider, controllerProvider, viewStylesheetProvider
        );
        this.navigator = new SimpleViewIdNavigator(stageProvider, viewProvider);

        Scene scene = new Scene(viewProvider.getViewById("problem-list"), 640, 360);
        scene.getStylesheets().add("/css/styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
