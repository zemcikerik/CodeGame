package dev.zemco.codegame.presentation.problems;

import dev.zemco.codegame.problems.IProblemService;
import dev.zemco.codegame.problems.Problem;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
class ProblemListModelTest {

    @Mock
    private IProblemService problemService;

    @Test
    void constructorShouldThrowIllegalArgumentExceptionWhenProblemServiceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ProblemListModel(null));
    }

    @Test
    void problemsPropertyShouldHoldProblemsProvidedByProblemService() {
        List<Problem> problems = List.of(mock(Problem.class), mock(Problem.class));
        when(this.problemService.getAllProblems()).thenReturn(problems);
        ProblemListModel model = new ProblemListModel(this.problemService);

        ObservableList<Problem> propertyValue = model.problemsProperty().get();

        assertThat(propertyValue, equalTo(problems));
    }

    @Test
    void selectProblemShouldSelectProvidedProblem() {
        Problem problem = mock(Problem.class);
        ProblemListModel model = new ProblemListModel(this.problemService);

        model.selectProblem(problem);

        assertThat(model.selectedProblemProperty().get(), equalTo(problem));
    }

    @Test
    void selectProblemShouldUnselectProblemWhenNullIsUsed() {
        ProblemListModel model = new ProblemListModel(this.problemService);
        model.selectProblem(mock(Problem.class));

        model.selectProblem(null);

        assertThat(model.selectedProblemProperty().get(), nullValue());
    }

}
