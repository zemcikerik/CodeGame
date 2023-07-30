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
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.WindowMatchers.isShowing;

@Tag(INTEGRATION_TEST)
@ExtendWith(ApplicationExtension.class)
class ApplicationItTest {

    @Start
    public void start(Stage stage) {
        new CodeGameApplication().start(stage);
    }

    @Test
    void applicationShouldDisplayCodeGameWindow(FxRobot robot) {
        verifyThat(robot.window("CodeGame"), isShowing());
    }

}
