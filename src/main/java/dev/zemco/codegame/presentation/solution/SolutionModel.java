package dev.zemco.codegame.presentation.solution;

import dev.zemco.codegame.compilation.IInstructionDescriptor;
import dev.zemco.codegame.compilation.IProgramCompiler;
import dev.zemco.codegame.compilation.InvalidSyntaxException;
import dev.zemco.codegame.compilation.Program;
import dev.zemco.codegame.evaluation.IEvaluationService;
import dev.zemco.codegame.evaluation.ISolutionEvaluator;
import dev.zemco.codegame.execution.NoNextInstructionException;
import dev.zemco.codegame.execution.StepExecutionException;
import dev.zemco.codegame.execution.memory.IMemory;
import dev.zemco.codegame.execution.memory.IMemoryCell;
import dev.zemco.codegame.presentation.errors.IProgramErrorModel;
import dev.zemco.codegame.presentation.errors.IProgramErrorModelFactory;
import dev.zemco.codegame.presentation.memory.IMemoryCellObserver;
import dev.zemco.codegame.presentation.memory.UpdatableMemoryCellObserverAdapter;
import dev.zemco.codegame.problems.Problem;
import dev.zemco.codegame.problems.ProblemCase;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

// TODO: either split this up into smaller classes or use state pattern
public class SolutionModel implements ISolutionModel {

    private final ReadOnlyObjectWrapper<Problem> problemProperty;
    private final ReadOnlyBooleanWrapper canCompileProperty;
    private final ReadOnlyBooleanWrapper canExecuteProperty;
    private final ReadOnlyBooleanWrapper executionRunningProperty;
    private final ReadOnlyBooleanWrapper executionAutoEnabledProperty;
    private final ReadOnlyBooleanWrapper canSubmitProperty;

    private final ReadOnlyObjectWrapper<Integer> nextInstructionLinePositionProperty;
    private final ReadOnlyObjectWrapper<ObservableList<IMemoryCellObserver>> memoryCellsProperty;

    private final ReadOnlyObjectWrapper<IProgramErrorModel> syntaxErrorProperty;
    private final ReadOnlyObjectWrapper<IProgramErrorModel> executionErrorProperty;

    private final IProgramErrorModelFactory programErrorModelFactory;
    private final IProgramCompiler programCompiler;
    private final IEvaluationService evaluationService;

    private String solutionCode;
    private Program program;
    private ISolutionEvaluator solutionEvaluator;
    private List<UpdatableMemoryCellObserverAdapter> cellObservers;

    public SolutionModel(
        IProgramErrorModelFactory programErrorModelFactory,
        IProgramCompiler programCompiler,
        IEvaluationService evaluationService
    ) {
        this.programErrorModelFactory = checkArgumentNotNull(
            programErrorModelFactory, "Program error model factory"
        );
        this.programCompiler = checkArgumentNotNull(programCompiler, "Program compiler");
        this.evaluationService = checkArgumentNotNull(evaluationService, "Evaluation service");

        this.program = null;
        this.solutionEvaluator = null;
        this.cellObservers = null;

        this.problemProperty = new ReadOnlyObjectWrapper<>(null);
        this.canCompileProperty = new ReadOnlyBooleanWrapper(true);
        this.canExecuteProperty = new ReadOnlyBooleanWrapper(false);
        this.executionRunningProperty = new ReadOnlyBooleanWrapper(false);
        this.executionAutoEnabledProperty = new ReadOnlyBooleanWrapper(false);
        this.canSubmitProperty = new ReadOnlyBooleanWrapper(false);

        this.nextInstructionLinePositionProperty = new ReadOnlyObjectWrapper<>(null);
        this.memoryCellsProperty = new ReadOnlyObjectWrapper<>(null);

        this.syntaxErrorProperty = new ReadOnlyObjectWrapper<>(null);
        this.executionErrorProperty = new ReadOnlyObjectWrapper<>(null);
    }

    @Override
    public void setProblem(Problem problem) {
        checkArgumentNotNull(problem, "Problem");
        this.problemProperty.set(problem);
        this.resetAttempt();
    }

    @Override
    public void compileSolution(String program) {
        if (!this.canCompileProperty.get()) {
            throw new IllegalStateException("Cannot compile program!");
        }

        checkArgumentNotNull(program, "Program");
        this.canCompileProperty.set(false);

        try {
            this.program = this.programCompiler.compileProgram(program);
        } catch (InvalidSyntaxException e) {
            IProgramErrorModel errorModel = this.programErrorModelFactory.createProgramErrorModel(e);
            this.syntaxErrorProperty.set(errorModel);
            return;
        }

        this.solutionCode = program;
        this.canExecuteProperty.set(true);
    }

    @Override
    public void startExecution() {
        if (!this.canExecuteProperty.get()) {
            throw new IllegalStateException("Cannot start program execution!");
        }

        // grab first problem case
        ProblemCase problemCase = this.problemProperty.get().getCases().get(0);
        this.solutionEvaluator = this.evaluationService.getEvaluatorForSolutionAttempt(this.program, problemCase);

        IMemory memory = this.solutionEvaluator.getExecutionContext().getMemory();
        this.observeMemoryCells(memory, problemCase);

        this.updateNextInstructionLinePositionFromEvaluator();
        this.executionRunningProperty.set(true);
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
    public void stepExecution() {
        if (!this.executionRunningProperty.get()) {
            throw new IllegalStateException("Cannot step execution while it is not running!");
        }

        try {
            this.solutionEvaluator.step();
        } catch (StepExecutionException | NoNextInstructionException e) {
            // this is required because of Javas static method overloading
            IProgramErrorModel errorModel = e instanceof StepExecutionException
                ? this.programErrorModelFactory.createProgramErrorModel((StepExecutionException)e)
                : this.programErrorModelFactory.createProgramErrorModel((NoNextInstructionException)e);

            this.executionErrorProperty.set(errorModel);
            this.stopExecution();
            return;
        }

        this.cellObservers.forEach(UpdatableMemoryCellObserverAdapter::updateValue);

        if (this.solutionEvaluator.canContinue()) {
            this.updateNextInstructionLinePositionFromEvaluator();
        } else {
            this.canSubmitProperty.set(this.solutionEvaluator.isSuccessful());
            this.stopExecution();
            this.canExecuteProperty.set(false);
        }
    }

    private void updateNextInstructionLinePositionFromEvaluator() {
        Integer linePosition = this.solutionEvaluator.getExecutionContext()
            .getExecutionEngine()
            .getNextInstructionDescriptor()
            .map(IInstructionDescriptor::getLinePosition)
            .orElse(null);

        this.nextInstructionLinePositionProperty.set(linePosition);
    }

    @Override
    public void stopExecution() {
        if (!this.executionRunningProperty.get()) {
            throw new IllegalStateException("Cannot stop execution while it is not running!");
        }

        this.executionRunningProperty.set(false);
        this.nextInstructionLinePositionProperty.set(null);
    }

    @Override
    public void submitSolution() {
        if (!this.canSubmitProperty.get()) {
            // TODO: message
            throw new IllegalStateException("Cannot submit attempt without validation!");
        }

        // TODO: implement me :)
        System.out.println("Attempting to submit a solution!");
        System.out.println(this.solutionCode);
    }

    @Override
    public void resetAttempt() {
        // TODO: implement this properly
        this.solutionCode = null;
        this.canCompileProperty.set(true);
        this.canExecuteProperty.set(false);
        this.canSubmitProperty.set(false);
        this.memoryCellsProperty.set(null);
        this.syntaxErrorProperty.set(null);
    }

    @Override
    public ObservableObjectValue<Problem> problemProperty() {
        return this.problemProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableBooleanValue canCompileProperty() {
        return this.canCompileProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableBooleanValue canExecuteProperty() {
        return this.canExecuteProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableBooleanValue canStepProperty() {
        return Bindings.and(this.executionRunningProperty, this.executionAutoEnabledProperty.not());
    }

    @Override
    public ObservableBooleanValue executionRunningProperty() {
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
    public ObservableObjectValue<IProgramErrorModel> syntaxErrorProperty() {
        return this.syntaxErrorProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableObjectValue<IProgramErrorModel> executionErrorProperty() {
        return this.executionErrorProperty.getReadOnlyProperty();
    }

}
