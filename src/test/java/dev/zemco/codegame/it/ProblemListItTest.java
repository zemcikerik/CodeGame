package dev.zemco.codegame.it;

import dev.zemco.codegame.CodeGameApplication;
import javafx.stage.Stage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static dev.zemco.codegame.TestConstants.INTEGRATION_TEST;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.matcher.control.ListViewMatchers.isEmpty;

@Tag(INTEGRATION_TEST)
@ExtendWith(ApplicationExtension.class)
class ProblemListItTest {

    private static final String PROBLEM_LIST = "#problemListView";
    private static final String DETAILS = "#detailBox";
    private static final String PROBLEM_NAME = "#problemNameLabel";
    private static final String PROBLEM_DESCRIPTION = "#problemDescriptionLabel";
    private static final String SOLVE = "Solve";

    private static final String CODE_AREA = "#codeArea";

    @Start
    public void start(Stage stage) {
        new CodeGameApplication().start(stage);
    }

    @Test
    void nonEmptyProblemListShouldBeDisplayed() {
        verifyThat(PROBLEM_LIST, isVisible());
        verifyThat(PROBLEM_LIST, not(isEmpty()));
    }

    @Test
    void problemDetailsShouldBeHiddenWhenNoProblemIsSelected() {
        verifyThat(DETAILS, isInvisible());
    }

    @Test
    void problemDetailsShouldDisplayDetailsOfSelectedProblem(FxRobot robot) {
        robot.clickOn(hasText(containsString("Test Problem 1")));
        verifyThat(PROBLEM_NAME, hasText("Test Problem 1"));
        verifyThat(PROBLEM_DESCRIPTION, hasText("Description"));
    }

    @Test
    void solveButtonShouldNavigateToSolutionView(FxRobot robot) {
        robot.clickOn(hasText(containsString("Test Problem 1"))).clickOn(SOLVE);
        verifyThat(robot.lookup(PROBLEM_LIST).tryQuery().isEmpty(), is(true));
        verifyThat(CODE_AREA, isVisible());
    }

}
