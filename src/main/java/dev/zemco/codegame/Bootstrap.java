package dev.zemco.codegame;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zemco.codegame.compilation.CodeProgramCompiler;
import dev.zemco.codegame.compilation.IProgramCompiler;
import dev.zemco.codegame.compilation.parsing.DelegatingInstructionParser;
import dev.zemco.codegame.compilation.parsing.FactorySingleAddressParameterInstructionParser;
import dev.zemco.codegame.compilation.parsing.FactorySingleIntegerParameterInstructionParser;
import dev.zemco.codegame.compilation.parsing.FactorySingleParameterInstructionParser;
import dev.zemco.codegame.compilation.parsing.IInstructionParser;
import dev.zemco.codegame.compilation.parsing.SupplierInstructionParser;
import dev.zemco.codegame.evaluation.EvaluationService;
import dev.zemco.codegame.evaluation.IEvaluationService;
import dev.zemco.codegame.evaluation.IEvaluationStrategy;
import dev.zemco.codegame.evaluation.OutputSinkSatisfiedEvaluationStrategy;
import dev.zemco.codegame.execution.IExecutionService;
import dev.zemco.codegame.execution.ProgramExecutionService;
import dev.zemco.codegame.execution.instructions.AdditionInstruction;
import dev.zemco.codegame.execution.instructions.CopyFromInstruction;
import dev.zemco.codegame.execution.instructions.CopyToInstruction;
import dev.zemco.codegame.execution.instructions.InputInstruction;
import dev.zemco.codegame.execution.instructions.JumpIfZeroInstruction;
import dev.zemco.codegame.execution.instructions.JumpInstruction;
import dev.zemco.codegame.execution.instructions.OutputInstruction;
import dev.zemco.codegame.execution.io.IInputSourceFactory;
import dev.zemco.codegame.execution.io.IOutputSinkFactory;
import dev.zemco.codegame.execution.io.IterableInputSource;
import dev.zemco.codegame.execution.io.VerifyingInputSourceToOutputSinkAdapter;
import dev.zemco.codegame.execution.memory.ConstantSizeMemory;
import dev.zemco.codegame.execution.memory.IMemoryCellFactory;
import dev.zemco.codegame.execution.memory.IMemoryFactory;
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
import dev.zemco.codegame.presentation.ImmutableStageProvider;
import dev.zemco.codegame.presentation.ImmutableViewStylesheetProvider;
import dev.zemco.codegame.presentation.ResourceFxmlViewSourceProvider;
import dev.zemco.codegame.presentation.SimpleViewIdNavigator;
import dev.zemco.codegame.presentation.dialog.IDialogService;
import dev.zemco.codegame.presentation.dialog.JavaFxDialogService;
import dev.zemco.codegame.presentation.highlighting.CodeHighlightStyleComputer;
import dev.zemco.codegame.presentation.highlighting.IHighlightStyleComputer;
import dev.zemco.codegame.presentation.problems.IProblemListModel;
import dev.zemco.codegame.presentation.problems.ProblemListController;
import dev.zemco.codegame.presentation.problems.ProblemListModel;
import dev.zemco.codegame.presentation.solution.ISolutionModel;
import dev.zemco.codegame.presentation.solution.SolutionController;
import dev.zemco.codegame.presentation.solution.SolutionModel;
import dev.zemco.codegame.presentation.solution.errors.ISolutionErrorModelFactory;
import dev.zemco.codegame.presentation.solution.errors.SimpleSolutionErrorModelFactory;
import dev.zemco.codegame.problems.IProblemRepository;
import dev.zemco.codegame.problems.UrlObjectMapperProblemRepository;
import dev.zemco.codegame.programs.IProgramBuilderFactory;
import dev.zemco.codegame.programs.ProgramBuilder;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Bootstraps all needed application components. If new components / dependencies are added to the project,
 * this is where they should be properly instantiated.
 * <p>
 * Originally we planned to use a dependency injection library Guice from Google, but we were unable to do so due to
 * file size constrains of the final fat jar.
 *
 * @author Erik Zemčík
 */
public final class Bootstrap {

    private final Class<?> resourceClass;
    private final Stage primaryStage;

    // we keep navigator in attribute because it is required inside the IControllerFactory,
    // but it was not instanced yet, as Navigator implementation depends on the mentioned factory
    // (we work around this by capturing this)
    private INavigator navigator;

    private Bootstrap(Class<?> resourceClass, Stage primaryStage) {
        this.resourceClass = checkArgumentNotNull(resourceClass, "Resource class");
        this.primaryStage = checkArgumentNotNull(primaryStage, "Primary stage");
    }

    /**
     * Bootstraps all needed application components with the provided resource class and primary stage.
     * This method returns a provider of views, which can be used to retrieve the first view presented to the user.
     *
     * @param resourceClass class to use for resource resolving
     * @param primaryStage primary stage of the application
     * @return view provider ready for use
     *
     * @throws IllegalArgumentException if {@code resourceClass} is {@code null} or
     *                                  if {@code primaryStage} is {@code null}
     */
    public static IViewProvider bootstrap(Class<?> resourceClass, Stage primaryStage) {
        return new Bootstrap(resourceClass, primaryStage).createViewProvider();
    }

    private IViewProvider createViewProvider() {
        IFxmlViewSourceProvider fxmlViewSourceProvider = this.createFxmlViewSourceProvider();
        IControllerFactory controllerFactory = this.createControllerFactory();
        IViewStylesheetProvider viewStylesheetProvider = this.createViewStylesheetProvider();

        IViewProvider viewProvider = new FxmlViewProvider(
            fxmlViewSourceProvider, controllerFactory, viewStylesheetProvider
        );

        IStageProvider stageProvider = new ImmutableStageProvider(this.primaryStage);
        this.navigator = new SimpleViewIdNavigator(stageProvider, viewProvider);

        return viewProvider;
    }

    private IControllerFactory createControllerFactory() {
        // dependencies for ProblemListModel
        IProblemRepository problemRepository = this.createProblemRepository();
        IProblemListModel problemListModel = new ProblemListModel(problemRepository);

        // dependencies for SolutionModel
        IProgramCompiler programCompiler = this.createProgramCompiler();
        IEvaluationService evaluationService = this.createEvaluationService();
        ISolutionErrorModelFactory solutionErrorModelFactory = new SimpleSolutionErrorModelFactory();

        ISolutionModel solutionModel = new SolutionModel(
            problemListModel, programCompiler, evaluationService, solutionErrorModelFactory
        );

        // dependencies for SolutionController
        IDialogService dialogService = new JavaFxDialogService();
        IHighlightStyleComputer highlightStyleComputer = this.createHighlightStyleComputer();

        // we only want to create controllers on the go, everything else is created beforehand and captured
        return controllerClass -> {
            checkArgumentNotNull(controllerClass, "Controller class");

            if (ProblemListController.class.isAssignableFrom(controllerClass)) {
                return new ProblemListController(problemListModel, this.navigator);
            } else if (SolutionController.class.isAssignableFrom(controllerClass)) {
                return new SolutionController(solutionModel, this.navigator, dialogService, highlightStyleComputer);
            }

            throw new IllegalStateException("Unknown controller class!");
        };
    }

    private IProblemRepository createProblemRepository() {
        URL problemListResource = this.resourceClass.getResource("/problems.json");
        ObjectMapper objectMapper = new ObjectMapper();

        return new UrlObjectMapperProblemRepository(problemListResource, objectMapper);
    }

    private IProgramCompiler createProgramCompiler() {
        IInstructionParser instructionParser = new DelegatingInstructionParser(List.of(
            new SupplierInstructionParser("in", InputInstruction::new),
            new SupplierInstructionParser("out", OutputInstruction::new),
            new FactorySingleIntegerParameterInstructionParser("add", AdditionInstruction::new),
            new FactorySingleParameterInstructionParser("jump", JumpInstruction::new),
            new FactorySingleParameterInstructionParser("jumpzero", JumpIfZeroInstruction::new),
            new FactorySingleAddressParameterInstructionParser("save", CopyToInstruction::new),
            new FactorySingleAddressParameterInstructionParser("load", CopyFromInstruction::new)
        ));

        IProgramBuilderFactory programBuilderFactory = ProgramBuilder::new;
        return new CodeProgramCompiler(instructionParser, programBuilderFactory);
    }

    private IEvaluationService createEvaluationService() {
        IExecutionService executionService = this.createExecutionService();
        IEvaluationStrategy evaluationStrategy = new OutputSinkSatisfiedEvaluationStrategy();

        return new EvaluationService(executionService, evaluationStrategy);
    }

    private IExecutionService createExecutionService() {
        IMemoryCellFactory memoryCellFactory = SimpleMemoryCell::new;
        IMemoryFactory memoryFactory = memorySize -> new ConstantSizeMemory(memorySize, memoryCellFactory);
        IMemoryService memoryService = new MemoryService(memoryFactory);

        IInputSourceFactory inputSourceFactory = IterableInputSource::new;
        IOutputSinkFactory outputSinkFactory = iterable ->
            new VerifyingInputSourceToOutputSinkAdapter(inputSourceFactory.createInputSourceFromIterable(iterable));

        return new ProgramExecutionService(memoryService, inputSourceFactory, outputSinkFactory);
    }

    private IHighlightStyleComputer createHighlightStyleComputer() {
        Set<String> instructionNames = Set.of("in", "out", "add", "jump", "jumpzero", "save", "load");
        return new CodeHighlightStyleComputer(instructionNames);
    }

    private IFxmlViewSourceProvider createFxmlViewSourceProvider() {
        return new ResourceFxmlViewSourceProvider(this.resourceClass, Map.of(
            "problem-list", "/fxml/ProblemListView.fxml",
            "solution", "/fxml/SolutionView.fxml"
        ));
    }

    private IViewStylesheetProvider createViewStylesheetProvider() {
        return new ImmutableViewStylesheetProvider(Map.of(
            "problem-list", List.of("/css/ProblemListView.css"),
            "solution", List.of("/css/SolutionView.css")
        ));
    }

}
