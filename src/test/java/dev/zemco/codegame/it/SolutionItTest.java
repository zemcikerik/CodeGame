package dev.zemco.codegame.it;

import dev.zemco.codegame.CodeGameApplication;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TextFlowMatchers;

import static dev.zemco.codegame.TestConstants.INTEGRATION_TEST;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.base.WindowMatchers.isFocused;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.matcher.control.TableViewMatchers.containsRow;

@Tag(INTEGRATION_TEST)
@ExtendWith(ApplicationExtension.class)
public class SolutionItTest {

    private static final String CODE_AREA = "#codeArea";
    private static final String PROBLEM_NAME = "#problemNameLabel";
    private static final String PROBLEM_DESCRIPTION = "#problemDescriptionLabel";
    private static final String COMPILE = "#compileButton";
    private static final String SUBMIT = "#submitButton";
    private static final String TOGGLE_EVALUATION = "#toggleEvaluation";
    private static final String STEP = "#stepButton";
    private static final String BACK = "#backButton";

    private static final String NEXT_INSTRUCTION_LINE = ".next-instruction-line";
    private static final String MEMORY_STATE_TABLE = "#memoryView";
    private static final String EXECUTION_ERROR = ".execution-error";

    private static final String DIALOG_TEXT = ".dialog-pane .content";
    private static final String DIALOG_CLOSE = ".dialog-pane .button";
    private static final String SYNTAX_ERROR_TITLE = "Syntax Error";
    private static final String SYNTAX_ERROR = ".syntax-error";
    private static final String EVALUATION_ERROR_TITLE = "Evaluation Failure";
    private static final String SUBMISSION_SUCCESS_TITLE = "Submission Success";
    private static final String EXECUTION_ERROR_TITLE = "Execution Error";

    private static final String PROBLEM_LIST = "#problemListView";
    private static final String VALID_SOLUTION = ">loop\nin\nout\njump loop";

    @Start
    public void start(Stage stage) {
        new CodeGameApplication().start(stage);
    }

    @BeforeEach
    public void setUp(FxRobot robot) {
        robot.clickOn(hasText(containsString("Test Problem 2"))).clickOn("Solve");
    }

    @AfterEach
    public void cleanupActiveDialogs(FxRobot robot) {
        // close all unclosed dialogs, in case any test with dialog fails
        robot.lookup(DIALOG_CLOSE).queryAll().forEach(robot::clickOn);
    }

    @Test
    public void problemDetailsShouldBeDisplayed() {
        verifyThat(PROBLEM_NAME, hasText("Test Problem 2"));
        verifyThat(PROBLEM_DESCRIPTION, hasText("other"));
    }

    @Test
    public void backShouldNavigateToProblemList(FxRobot robot) {
        robot.clickOn(BACK);
        verifyThat(robot.lookup(CODE_AREA).tryQuery().isEmpty(), is(true));
        verifyThat(PROBLEM_LIST, isVisible());
    }

    @Test
    public void invalidProgramShouldFailCompilation(FxRobot robot) {
        robot.clickOn(CODE_AREA).write("in\nout 2");
        robot.clickOn(COMPILE);

        verifyThat(robot.window(SYNTAX_ERROR_TITLE), isFocused());
        verifyThat(DIALOG_TEXT, hasText(containsString("syntax error on line 2!")));

        robot.clickOn(DIALOG_CLOSE);

        verifyThat(SYNTAX_ERROR, TextFlowMatchers.hasText("out 2"));
        verifyThat(COMPILE, isDisabled());
        verifyThat(SUBMIT, isDisabled());
    }

    @Test
    public void programModificationShouldResetAttempt(FxRobot robot) {
        robot.clickOn(CODE_AREA).write("in").clickOn(COMPILE);
        verifyThat(COMPILE, isDisabled());

        robot.clickOn(CODE_AREA).write("\nout");
        verifyThat(COMPILE, isEnabled());
    }

    @Test
    public void invalidSolutionShouldFailSubmission(FxRobot robot) {
        robot.clickOn(CODE_AREA).write("in\nadd 1\nout");
        robot.clickOn(COMPILE).clickOn(SUBMIT);

        verifyThat(robot.window(EVALUATION_ERROR_TITLE), isFocused());
        robot.clickOn(DIALOG_CLOSE);

        verifyThat(SUBMIT, isDisabled());
    }

    @Test
    public void solutionContainingInfiniteLoopShouldFailSubmission(FxRobot robot) {
        robot.clickOn(CODE_AREA).write(">loop\njump loop");
        robot.clickOn(COMPILE).clickOn(SUBMIT);

        verifyThat(robot.window(EVALUATION_ERROR_TITLE), isFocused());
        robot.clickOn(DIALOG_CLOSE);

        verifyThat(SUBMIT, isDisabled());
    }

    @Test
    public void validSolutionShouldSuccessfullyPassSubmission(FxRobot robot) {
        robot.clickOn(CODE_AREA).write(VALID_SOLUTION);
        robot.clickOn(COMPILE).clickOn(SUBMIT);

        verifyThat(robot.window(SUBMISSION_SUCCESS_TITLE), isFocused());
        robot.clickOn(DIALOG_CLOSE);

        verifyThat(robot.lookup(CODE_AREA).tryQuery().isEmpty(), is(true));
        verifyThat(PROBLEM_LIST, isVisible());
    }

    @Test
    public void testEvaluationCanBeStarted(FxRobot robot) {
        robot.clickOn(COMPILE);
        verifyThat(TOGGLE_EVALUATION, isEnabled());
        verifyThat(STEP, isDisabled());

        robot.clickOn(TOGGLE_EVALUATION);
        verifyThat(BACK, isDisabled());
        verifyThat(STEP, isEnabled());
    }

    @Test
    public void testEvaluationCanBeStepped(FxRobot robot) {
        robot.clickOn(CODE_AREA).write(VALID_SOLUTION);
        robot.clickOn(COMPILE).clickOn(TOGGLE_EVALUATION);

        verifyThat(NEXT_INSTRUCTION_LINE, TextFlowMatchers.hasText("in"));
        robot.clickOn(STEP);
        verifyThat(NEXT_INSTRUCTION_LINE, TextFlowMatchers.hasText("out"));
        robot.clickOn(STEP);
        verifyThat(NEXT_INSTRUCTION_LINE, TextFlowMatchers.hasText("jump loop"));
    }

    @Test
    public void testEvaluationShouldDisplayMemoryStateOfUnderlyingExecution(FxRobot robot) {
        robot.clickOn(CODE_AREA).write(VALID_SOLUTION);
        robot.clickOn(COMPILE).clickOn(TOGGLE_EVALUATION);

        verifyThat(MEMORY_STATE_TABLE, containsRow("Working Cell", "-"));
        robot.clickOn(STEP);
        verifyThat(MEMORY_STATE_TABLE, containsRow("Working Cell", "1"));
    }

    @Test
    public void testEvaluationShouldDisplayErrorInfoIfUnderlyingExecutionFails(FxRobot robot) {
        robot.clickOn(CODE_AREA).write("out");
        robot.clickOn(COMPILE).clickOn(TOGGLE_EVALUATION);
        robot.clickOn(STEP);

        verifyThat(robot.window(EXECUTION_ERROR_TITLE), isFocused());
        verifyThat(DIALOG_TEXT, hasText(allOf(
            containsString("execution on line 1!"),
            containsString("Working cell does not hold value!")
        )));
        robot.clickOn(DIALOG_CLOSE);

        verifyThat(EXECUTION_ERROR, TextFlowMatchers.hasText("out"));
        verifyThat(STEP, isDisabled());
        verifyThat(SUBMIT, isDisabled());
    }

    @Test
    public void testEvaluationShouldDisplayErrorInfoWhenNoNextInstructionAvailable(FxRobot robot) {
        robot.clickOn(COMPILE).clickOn(TOGGLE_EVALUATION);
        robot.clickOn(STEP);

        verifyThat(robot.window(EXECUTION_ERROR_TITLE), isFocused());
        verifyThat(DIALOG_TEXT, hasText(containsString("No next instruction")));
        robot.clickOn(DIALOG_CLOSE);

        verifyThat(STEP, isDisabled());
        verifyThat(SUBMIT, isDisabled());
    }

    @Test
    public void testEvaluationShouldHandleMemoryManipulation(FxRobot robot) {
        robot.clickOn(CODE_AREA).write("load 2\nadd 12\nsave 1");
        robot.clickOn(COMPILE).clickOn(TOGGLE_EVALUATION);
        robot.clickOn(STEP).clickOn(STEP).clickOn(STEP);

        verifyThat(MEMORY_STATE_TABLE, containsRow("1", "-99988"));
    }

}
