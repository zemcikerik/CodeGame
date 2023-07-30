package dev.zemco.codegame.problems;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
class ProblemServiceTest {

    @Test
    void constructorShouldThrowIfProblemRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ProblemService(null));
    }

    @Test
    void getAllProblemsShouldReturnProblemsFromProblemRepository() {
        List<Problem> problems = List.of(mock(Problem.class));
        IProblemRepository repository = mock(IProblemRepository.class);
        when(repository.getAllProblems()).thenReturn(problems);
        ProblemService service = new ProblemService(repository);

        List<Problem> result = service.getAllProblems();

        assertThat(result, equalTo(result));
    }

}
