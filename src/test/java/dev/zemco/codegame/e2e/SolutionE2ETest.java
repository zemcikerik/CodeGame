package dev.zemco.codegame.e2e;

import dev.zemco.codegame.CodeGameApplication;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static dev.zemco.codegame.TestConstants.E2E_TEST;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.base.WindowMatchers.isFocused;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@Tag(E2E_TEST)
@ExtendWith(ApplicationExtension.class)
public class SolutionE2ETest {

    private static final String CODE_AREA = "#codeArea";
    private static final String PROBLEM_NAME = "#problemNameLabel";
    private static final String PROBLEM_DESCRIPTION = "#problemDescriptionLabel";
    private static final String COMPILE = "#compileButton";
    private static final String SUBMIT = "#submitButton";
    private static final String TOGGLE_EVALUATION = "#toggleEvaluation";
    private static final String STEP = "#stepButton";
    private static final String BACK = "#backButton";

    private static final String DIALOG_TEXT = ".dialog-pane .content";
    private static final String DIALOG_CLOSE = ".dialog-pane .button";
    private static final String SYNTAX_ERROR_TITLE = "Syntax Error";
    private static final String SYNTAX_ERROR = ".syntax-error";
    private static final String EVALUATION_ERROR_TITLE = "Evaluation Failure";
    private static final String SUBMISSION_SUCCESS_TITLE = "Submission Success";

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

        verifyThat(SYNTAX_ERROR, isVisible());
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

}
