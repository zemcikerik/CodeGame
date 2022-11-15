package dev.zemco.codegame.presentation.solution;

import dev.zemco.codegame.compilation.IProgramCompiler;
import dev.zemco.codegame.compilation.InvalidSyntaxException;
import dev.zemco.codegame.problems.Problem;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;

import java.util.UUID;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class SolutionModel implements ISolutionModel {

    private final IProgramCompiler programCompiler;
    private final SimpleBooleanProperty canCompileProperty;
    private final SimpleBooleanProperty canExecuteProperty;
    private final SimpleBooleanProperty executionRunningProperty;
    private final SimpleBooleanProperty executionAutoEnabledProperty;
    private final SimpleObjectProperty<SyntaxErrorModel> syntaxErrorProperty;

    public SolutionModel(IProgramCompiler programCompiler) {
        this.programCompiler = checkArgumentNotNull(programCompiler, "Program compiler");
        this.canCompileProperty = new SimpleBooleanProperty(true);
        this.canExecuteProperty = new SimpleBooleanProperty(false);
        this.executionRunningProperty = new SimpleBooleanProperty(false);
        this.executionAutoEnabledProperty = new SimpleBooleanProperty(false);
        this.syntaxErrorProperty = new SimpleObjectProperty<>(null);
    }

    @Override
    public void submitAttemptForCompilation(String program) {
        if (!this.canCompileProperty.get()) {
            // TODO: message
            throw new IllegalStateException();
        }

        this.canCompileProperty.set(false);

        try {
            this.programCompiler.compileProgram(program);
        } catch (InvalidSyntaxException e) {
            this.syntaxErrorProperty.set(new SyntaxErrorModel(e.getLinePosition(), e.getMessage()));
            return;
        }

        this.canExecuteProperty.set(true);
    }

    @Override
    public void startExecution() {
        if (!this.canExecuteProperty.get()) {
            // TODO: message
            throw new IllegalStateException();
        }

        this.executionRunningProperty.set(true);
    }

    @Override
    public void stopExecution() {
        if (!this.executionRunningProperty.get()) {
            // TODO: message
            throw new IllegalStateException();
        }

        this.executionRunningProperty.set(false);
    }

    @Override
    public void resetAttempt() {
        this.canCompileProperty.set(true);
        this.canExecuteProperty.set(false);
        this.syntaxErrorProperty.set(null);
        // this.executionRunningProperty.set(false);
    }

    @Override
    public Problem getProblem() {
        return new Problem(UUID.randomUUID(), "Hello, World", "Description", 0, null);
    }

    @Override
    public ObservableBooleanValue canCompileProperty() {
        return this.canCompileProperty;
    }

    @Override
    public ObservableBooleanValue canExecuteProperty() {
        return this.canExecuteProperty;
    }

    @Override
    public ObservableBooleanValue canStepProperty() {
        return Bindings.and(this.executionRunningProperty, this.executionAutoEnabledProperty.not());
    }

    @Override
    public ObservableBooleanValue executionRunningProperty() {
        return this.executionRunningProperty;
    }

    @Override
    public ObservableValue<SyntaxErrorModel> syntaxErrorProperty() {
        return this.syntaxErrorProperty;
    }

}
