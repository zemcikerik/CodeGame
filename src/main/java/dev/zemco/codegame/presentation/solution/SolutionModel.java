package dev.zemco.codegame.presentation.solution;

import dev.zemco.codegame.evaluation.StepEvaluationException;
import dev.zemco.codegame.presentation.problems.IProblemListModel;
import dev.zemco.codegame.programs.InstructionDescriptor;
import dev.zemco.codegame.compilation.IProgramCompiler;
import dev.zemco.codegame.compilation.InvalidSyntaxException;
import dev.zemco.codegame.programs.Program;
import dev.zemco.codegame.evaluation.IEvaluationService;
import dev.zemco.codegame.evaluation.ISolutionEvaluator;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;
import dev.zemco.codegame.presentation.solution.errors.ISolutionErrorModel;
import dev.zemco.codegame.presentation.solution.errors.ISolutionErrorModelFactory;
import dev.zemco.codegame.presentation.execution.IMemoryCellObserver;
import dev.zemco.codegame.presentation.execution.UpdatableMemoryCellObserverAdapter;
import dev.zemco.codegame.problems.Problem;
import dev.zemco.codegame.problems.ProblemCase;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Implementation of {@link SolutionModel solution model} evaluating solution created by player.
 * Target {@link Problem problem} is provided by the {@link IProblemListModel problem list model}.
 * This model uses the provided {@link IProgramCompiler} for compilation and {@link IEvaluationService} for evaluation.
 *
 * @author Erik Zemčík
 * @see ISolutionModel
 */
public class SolutionModel implements ISolutionModel {

    private final ObservableObjectValue<Problem> problemProperty;
    private final ReadOnlyBooleanWrapper canCompileProperty;
    private final ReadOnlyBooleanWrapper canEvaluateProperty;
    private final ReadOnlyBooleanWrapper executionRunningProperty;
    private final ReadOnlyBooleanWrapper canSubmitProperty;

    private final ReadOnlyObjectWrapper<Integer> nextInstructionLinePositionProperty;
    private final ReadOnlyObjectWrapper<ObservableList<IMemoryCellObserver>> memoryCellsProperty;

    private final ReadOnlyObjectWrapper<ISolutionErrorModel> syntaxErrorProperty;
    private final ReadOnlyObjectWrapper<ISolutionErrorModel> executionErrorProperty;

    private final ISolutionErrorModelFactory programErrorModelFactory;
    private final IProgramCompiler programCompiler;
    private final IEvaluationService evaluationService;

    private Program solution;
    private ISolutionEvaluator solutionEvaluator;
    private List<UpdatableMemoryCellObserverAdapter> cellObservers;

    /**
     * Creates an instance of {@link SolutionModel}.
     *
     * @param problemListModel source of the target {@link Problem problem}
     * @param programCompiler compiler for compilation of player's solution source code
     * @param evaluationService service for evaluating the player's solution
     * @param programErrorModelFactory factory for creating errors indicating details about failure of player's solution
     *
     * @throws IllegalArgumentException if any parameter is {@code null}
     */
    public SolutionModel(
        IProblemListModel problemListModel,
        IProgramCompiler programCompiler,
        IEvaluationService evaluationService,
        ISolutionErrorModelFactory programErrorModelFactory
    ) {
        checkArgumentNotNull(problemListModel, "Problem list model");
        this.programCompiler = checkArgumentNotNull(programCompiler, "Program compiler");
        this.evaluationService = checkArgumentNotNull(evaluationService, "Evaluation service");
        this.programErrorModelFactory = checkArgumentNotNull(
            programErrorModelFactory, "Program error model factory"
        );

        this.problemProperty = problemListModel.selectedProblemProperty();
        this.canCompileProperty = new ReadOnlyBooleanWrapper();
        this.canEvaluateProperty = new ReadOnlyBooleanWrapper();
        this.executionRunningProperty = new ReadOnlyBooleanWrapper();
        this.canSubmitProperty = new ReadOnlyBooleanWrapper();

        this.nextInstructionLinePositionProperty = new ReadOnlyObjectWrapper<>();
        this.memoryCellsProperty = new ReadOnlyObjectWrapper<>();

        this.syntaxErrorProperty = new ReadOnlyObjectWrapper<>();
        this.executionErrorProperty = new ReadOnlyObjectWrapper<>();

        this.resetAttempt();
    }

    @Override
    public void resetAttempt() {
        this.canCompileProperty.set(true);
        this.canEvaluateProperty.set(false);
        this.executionRunningProperty.set(false);
        this.canSubmitProperty.set(false);

        this.nextInstructionLinePositionProperty.set(null);
        this.memoryCellsProperty.set(null);

        this.syntaxErrorProperty.set(null);
        this.executionErrorProperty.set(null);

        this.solution = null;
        this.solutionEvaluator = null;
        this.cellObservers = null;
    }

    @Override
    public void compileSolution(String sourceCode) {
        checkArgumentNotNull(sourceCode, "Source code");
        this.checkValidState(this.canCompileProperty, "compile solution");

        this.canCompileProperty.set(false);

        try {
            this.solution = this.programCompiler.compileProgram(sourceCode);
        } catch (InvalidSyntaxException e) {
            this.syntaxErrorProperty.set(this.programErrorModelFactory.createSolutionErrorModel(e));
            return;
        }

        this.canEvaluateProperty.set(true);
        this.canSubmitProperty.set(true);
    }

    @Override
    public void startTestEvaluation() {
        this.checkValidState(this.canEvaluateProperty, "start test evaluation");

        ProblemCase problemCase = this.getTestProblemCase();
        this.solutionEvaluator = this.evaluationService.getEvaluatorForProblemCaseSolution(this.solution, problemCase);

        IMemory memory = this.solutionEvaluator.getExecutionContext().getMemory();
        this.observeMemoryCells(memory, problemCase);

        this.updateNextInstructionLinePositionFromEvaluator();
        this.executionRunningProperty.set(true);
    }

    private ProblemCase getTestProblemCase() {
        // every problem is guaranteed to have at least one problem case
        return this.problemProperty.get().getCases().get(0);
    }

    private void observeMemoryCells(IMemory memory, ProblemCase problemCase) {
        this.cellObservers = new ArrayList<>();

        int memorySize = problemCase.getMemorySettings().getSize();
        for (int address = 0; address < memorySize; address++) {
            IMemoryCell memoryCell = memory.getCellByAddress(address);
            this.cellObservers.add(new UpdatableMemoryCellObserverAdapter(address, memoryCell));
        }

        this.memoryCellsProperty.set(FXCollections.observableList(List.copyOf(this.cellObservers)));
    }

    @Override
    public void stepTestEvaluation() {
        this.checkValidState(this.executionRunningProperty, "step test execution");

        try {
            this.solutionEvaluator.step();
        } catch (StepEvaluationException e) {
            this.executionErrorProperty.set(this.programErrorModelFactory.createSolutionErrorModel(e));
            this.stopTestEvaluation();
            this.canSubmitProperty.set(false);
            return;
        }

        // update memory cell observers as the state of the underlying execution may have changed due to the step
        this.cellObservers.forEach(UpdatableMemoryCellObserverAdapter::updateValue);

        if (this.solutionEvaluator.hasFinished()) {
            this.stopTestEvaluation();
        } else {
            this.updateNextInstructionLinePositionFromEvaluator();
        }
    }

    private void updateNextInstructionLinePositionFromEvaluator() {
        Integer linePosition = this.solutionEvaluator.getExecutionContext()
            .getExecutionEngine()
            .getNextInstructionDescriptor()
            .map(InstructionDescriptor::getLinePosition)
            .orElse(null);

        this.nextInstructionLinePositionProperty.set(linePosition);
    }

    @Override
    public void stopTestEvaluation() {
        this.checkValidState(this.executionRunningProperty, "stop test evaluation");

        this.executionRunningProperty.set(false);
        this.nextInstructionLinePositionProperty.set(null);
    }

    @Override
    public boolean submitSolution() {
        this.checkValidState(this.canSubmitProperty, "submit solution");

        this.canSubmitProperty.set(false);

        Problem problem = this.problemProperty.get();
        return this.evaluationService.evaluateSolutionOnAllProblemCases(this.solution, problem);
    }

    private void checkValidState(ObservableBooleanValue stateValidProperty, String operationName) {
        if (!stateValidProperty.get()) {
            String message = String.format("Solution model cannot currently %s!", operationName);
            throw new IllegalStateException(message);
        }
    }

    @Override
    public ObservableObjectValue<Problem> problemProperty() {
        return this.problemProperty;
    }

    @Override
    public ObservableBooleanValue canCompileProperty() {
        return this.canCompileProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableBooleanValue canEvaluateProperty() {
        return this.canEvaluateProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableBooleanValue canStepProperty() {
        return this.executionRunningProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableBooleanValue evaluationRunningProperty() {
        return this.executionRunningProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableBooleanValue canSubmitProperty() {
        return this.canSubmitProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableObjectValue<Integer> nextInstructionLinePositionProperty() {
        return this.nextInstructionLinePositionProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableObjectValue<ObservableList<IMemoryCellObserver>> memoryCellsProperty() {
        return this.memoryCellsProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableObjectValue<ISolutionErrorModel> syntaxErrorProperty() {
        return this.syntaxErrorProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableObjectValue<ISolutionErrorModel> executionErrorProperty() {
        return this.executionErrorProperty.getReadOnlyProperty();
    }

}
