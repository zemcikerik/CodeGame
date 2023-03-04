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
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.WindowMatchers.isFocused;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@Tag(E2E_TEST)
@ExtendWith(ApplicationExtension.class)
public class SolutionE2ETest {

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
        verifyThat("#problemNameLabel", hasText("Test Problem 2"));
        verifyThat("#problemDescriptionLabel", hasText("other"));
    }

    @Test
    public void invalidProgramShouldFailCompilation(FxRobot robot) {
        robot.clickOn("#codeArea").write("in\nout 2");
        robot.clickOn("Compile");

        verifyThat(robot.window("Syntax Error"), isFocused());
        verifyThat(".dialog-pane .content", hasText(containsString("syntax error on line 2!")));
    }

}
