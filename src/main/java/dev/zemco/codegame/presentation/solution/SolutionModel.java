package dev.zemco.codegame.presentation.solution;

import dev.zemco.codegame.compilation.IProgramCompiler;
import dev.zemco.codegame.compilation.InvalidSyntaxException;
import dev.zemco.codegame.compilation.Program;
import dev.zemco.codegame.execution.IExecutionContext;
import dev.zemco.codegame.execution.IExecutionService;
import dev.zemco.codegame.execution.memory.IMemory;
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

public class SolutionModel implements ISolutionModel {

    private final ReadOnlyObjectWrapper<Problem> problemProperty;
    private final ReadOnlyBooleanWrapper canCompileProperty;
    private final ReadOnlyBooleanWrapper canExecuteProperty;
    private final ReadOnlyBooleanWrapper executionRunningProperty;
    private final ReadOnlyBooleanWrapper executionAutoEnabledProperty;

    private final ReadOnlyObjectWrapper<ObservableList<IMemoryCellObserver>> memoryCellsProperty;
    private final ReadOnlyObjectWrapper<IProgramErrorModel> syntaxErrorProperty;

    private final IProgramErrorModelFactory programErrorModelFactory;
    private final IProgramCompiler programCompiler;
    private final IExecutionService executionService;

    private Program program;
    private IExecutionContext executionContext;
    private List<UpdatableMemoryCellObserverAdapter> cellObservers;

    public SolutionModel(
        IProgramErrorModelFactory programErrorModelFactory,
        IProgramCompiler programCompiler,
        IExecutionService executionService
    ) {
        this.programErrorModelFactory = checkArgumentNotNull(
            programErrorModelFactory, "Program error model factory"
        );
        this.programCompiler = checkArgumentNotNull(programCompiler, "Program compiler");
        this.executionService = checkArgumentNotNull(executionService, "Execution service");
        this.program = null;

        this.problemProperty = new ReadOnlyObjectWrapper<>(null);
        this.canCompileProperty = new ReadOnlyBooleanWrapper(true);
        this.canExecuteProperty = new ReadOnlyBooleanWrapper(false);
        this.executionRunningProperty = new ReadOnlyBooleanWrapper(false);
        this.executionAutoEnabledProperty = new ReadOnlyBooleanWrapper(false);

        this.memoryCellsProperty = new ReadOnlyObjectWrapper<>(null);
        this.syntaxErrorProperty = new ReadOnlyObjectWrapper<>(null);
    }

    @Override
    public void setProblem(Problem problem) {
        this.problemProperty.set(problem);
    }

    @Override
    public void submitAttemptForCompilation(String program) {
        if (!this.canCompileProperty.get()) {
            throw new IllegalStateException("Cannot compile program!");
        }

        this.canCompileProperty.set(false);

        try {
            this.program = this.programCompiler.compileProgram(program);
        } catch (InvalidSyntaxException e) {
            IProgramErrorModel programErrorModel = this.programErrorModelFactory.createProgramErrorModel(e);
            this.syntaxErrorProperty.set(programErrorModel);
            return;
        }

        this.canExecuteProperty.set(this.program != null);
    }

    // TODO: refactor and move implementation content
    @Override
    public void startExecution() {
        if (!this.canExecuteProperty.get()) {
            throw new IllegalStateException("Cannot start program execution!");
        }

        // grab first problem case
        ProblemCase problemCase = this.problemProperty.get().getCases().get(0);
        this.executionContext = this.executionService.getExecutionContextForSolutionAttempt(problemCase, this.program);

        IMemory memory = this.executionContext.getMemory();
        int memorySize = problemCase.getMemorySettings().getSize();

        // observe all memory cells
        this.cellObservers = new ArrayList<>();

        for (int address = 0; address < memorySize; address++) {
            this.cellObservers.add(new UpdatableMemoryCellObserverAdapter(address, memory.getCellByAddress(address)));
        }

        this.memoryCellsProperty.set(FXCollections.observableList(new ArrayList<>(this.cellObservers)));
        this.executionRunningProperty.set(true);
    }

    @Override
    public void stepExecution() {
        if (!this.executionRunningProperty.get()) {
            throw new IllegalStateException("Cannot step execution while it is not running!");
        }

        this.executionContext.getExecutionEngine().step();
        this.cellObservers.forEach(UpdatableMemoryCellObserverAdapter::updateValue);
    }

    @Override
    public void stopExecution() {
        if (!this.executionRunningProperty.get()) {
            throw new IllegalStateException("Cannot stop execution while it is not running!");
        }

        this.executionRunningProperty.set(false);
    }

    @Override
    public void resetAttempt() {
        this.canCompileProperty.set(true);
        this.canExecuteProperty.set(false);
        this.memoryCellsProperty.set(null);
        this.syntaxErrorProperty.set(null);
        // TODO: this.executionRunningProperty.set(false);
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
    public ObservableObjectValue<ObservableList<IMemoryCellObserver>> memoryCellsProperty() {
        return this.memoryCellsProperty.getReadOnlyProperty();
    }

    @Override
    public ObservableObjectValue<IProgramErrorModel> syntaxErrorProperty() {
        return this.syntaxErrorProperty.getReadOnlyProperty();
    }

}
