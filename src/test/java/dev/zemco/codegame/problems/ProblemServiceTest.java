package dev.zemco.codegame.problems;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProblemServiceTest {

    @Test
    public void constructorShouldThrowIfProblemRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ProblemService(null));
    }

    @Test
    public void getAllProblemsShouldReturnProblemsFromProblemRepository() {
        List<Problem> problems = List.of(mock(Problem.class));
        IProblemRepository repository = mock(IProblemRepository.class);
        when(repository.getAllProblems()).thenReturn(problems);
        ProblemService service = new ProblemService(repository);

        List<Problem> result = service.getAllProblems();

        assertThat(result, equalTo(result));
    }

}
