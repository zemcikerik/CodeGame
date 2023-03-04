package dev.zemco.codegame.e2e;

import dev.zemco.codegame.CodeGameApplication;
import javafx.stage.Stage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static dev.zemco.codegame.TestConstants.E2E_TEST;
import static org.hamcrest.Matchers.not;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.ListViewMatchers.isEmpty;

@Tag(E2E_TEST)
@ExtendWith(ApplicationExtension.class)
public class ProblemListE2ETest {

    @Start
    public void start(Stage stage) {
        new CodeGameApplication().start(stage);
    }

    @Test
    public void nonEmptyProblemListShouldBeDisplayed() {
        verifyThat("#problemListView", isVisible());
        verifyThat("#problemListView", not(isEmpty()));
    }

    @Test
    public void problemDetailsShouldBeHiddenWhenNoProblemIsSelected() {
        verifyThat("#detailBox", isInvisible());
    }

}
