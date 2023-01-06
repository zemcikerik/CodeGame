package dev.zemco.codegame.presentation.solution;

import dev.zemco.codegame.presentation.solution.errors.ISolutionErrorModel;
import dev.zemco.codegame.presentation.execution.IMemoryCellObserver;
import dev.zemco.codegame.problems.Problem;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;

/**
 * Manages logic and state related to user's solution to a given {@link Problem problem}.
 * {@link Problem} being solved is accessible via {@link #problemProperty()}.
 * <p>
 * A new solution should be triggered by {@link #resetAttempt()}.
 * Each solution attempt to be compiled via {@link #compileSolution(String)}. Availability of compilation
 * should be checked through {@link #canCompileProperty()}. If the compilation fails, related error
 * information is accessible through {@link #syntaxErrorProperty()}.
 * <p>
 * For a given solution attempt, test evaluation is possible, and can be started using {@link #startTestEvaluation()}.
 * Availability of starting the test evaluation should be checked through {@link #canEvaluateProperty()}.
 * Information about the state of the evaluation is available through {@link #nextInstructionLinePositionProperty()},
 * {@link #memoryCellsProperty()}, and {@link #evaluationRunningProperty()}.
 * Evaluation can be stepped via {@link #stepTestEvaluation()}, when indicated via {@link #canStepProperty()}.
 * If the evaluation encounters an execution error, the error becomes available through
 * {@link #executionErrorProperty()}. Evaluation can be stopped via {@link #stopTestEvaluation()}.
 * <p>
 * Solution can be submitted via {@link #submitSolution()}, when indicated via {@link #canSubmitProperty()}.
 *
 * @author Erik Zemčík
 */
public interface ISolutionModel {

    /**
     * Triggers a new solution attempt.
     */
    void resetAttempt();

    /**
     * Attempts to compile the player's solution.
     * Availability of compilation can be checked via the {@link #canCompileProperty() can compile property}.
     *
     * @param sourceCode source code of the player's solution
     *
     * @throws IllegalArgumentException if {@code sourceCode} is {@code null}
     * @throws IllegalStateException if compilation is not available
     */
    void compileSolution(String sourceCode);

    /**
     * Starts test evaluation of the player's solution on test problem case.
     * Availability of test evaluation can be checked via the {@link #canEvaluateProperty() can evaluate property}.
     *
     * @throws IllegalStateException if starting of test evaluation is not available
     */
    void startTestEvaluation();

    /**
     * Steps test evaluation of the player's solution.
     * Test evaluation can be stepped when it is {@link #evaluationRunningProperty() running}.
     *
     * @throws IllegalStateException if the execution is not running
     */
    void stepTestEvaluation();

    /**
     * Stops test evaluation of the player's solution.
     * Test evaluation can be stopped when it is {@link #evaluationRunningProperty() running}.
     *
     * @throws IllegalStateException if the execution is not running
     */
    void stopTestEvaluation();

    /**
     * Attempts to submit the player's solution. If the submission succeeds, the solution
     * is considered valid for the given {@link Problem problem}.
     * Availability of submission can be checked via the {@link #canSubmitProperty() can submit property}.
     *
     * @return true if the submission succeeded, else false
     */
    boolean submitSolution();

    /**
     * Property holding the target {@link Problem problem} that is being solved.
     * @return read-only property containing target problem
     */
    ObservableObjectValue<Problem> problemProperty();

    /**
     * Property indicating if compilation of player's solution is available.
     * @return read-only property indicating availability of compilation
     */
    ObservableBooleanValue canCompileProperty();

    /**
     * Property indicating if test evaluation of player's solution is available.
     * @return read-only property indicating availability of test evaluation
     */
    ObservableBooleanValue canEvaluateProperty();

    /**
     * Property indicating if test evaluation of player's solution can be stepped.
     * @return read-only property indicating if test evaluation can be stepped
     */
    ObservableBooleanValue canStepProperty();

    /**
     * Property indicating if test evaluation of player's solution is running.
     * @return read-only property indicating if test evaluation is running
     */
    ObservableBooleanValue evaluationRunningProperty();

    /**
     * Property indicating if of player's solution can be submitted.
     * @return read-only property indicating if solution submission is available
     */
    ObservableBooleanValue canSubmitProperty();

    /**
     * Property indicating next instruction line position to execute during test evaluation of player's solution.
     * This property may hold {@code null} when the position is not available.
     *
     * @return read-only property indicating next instruction line position for evaluation
     */
    ObservableObjectValue<Integer> nextInstructionLinePositionProperty();

    /**
     * Property holding {@link IMemoryCellObserver memory cell observers} for observing memory state during the
     * test evaluation. This property may hold {@code null} when the evaluation is not running.
     *
     * @return read-only property holding {@link IMemoryCellObserver memory cell observers}
     */
    ObservableObjectValue<ObservableList<IMemoryCellObserver>> memoryCellsProperty();

    /**
     * Property holding the last {@link ISolutionModel solution error model} indicating syntax error
     * in the player's solution source code. This property may hold {@code null} if no syntax error was
     * found for the current solution.
     *
     * @return read-only property holding {@link ISolutionModel solution error model} indicating syntax error
     */
    ObservableObjectValue<ISolutionErrorModel> syntaxErrorProperty();

    /**
     * Property holding the last {@link ISolutionModel solution error model} indicating error during execution
     * of the test evaluation of player's solution. This property may hold {@code null} if no execution error was
     * found at the current state of evaluation.
     *
     * @return read-only property holding {@link ISolutionModel solution error model} indicating syntax error
     */
    ObservableObjectValue<ISolutionErrorModel> executionErrorProperty();

}
