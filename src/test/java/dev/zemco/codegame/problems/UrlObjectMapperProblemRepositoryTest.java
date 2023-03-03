package dev.zemco.codegame.problems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import dev.zemco.codegame.resources.ResourceLoadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
@ExtendWith(MockitoExtension.class)
public class UrlObjectMapperProblemRepositoryTest {

    @Mock
    private URL url;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        lenient().when(this.objectMapper.getTypeFactory()).thenReturn(TypeFactory.defaultInstance());
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfUrlIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new UrlObjectMapperProblemRepository(null, this.objectMapper)
        );
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfObjectMapperIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new UrlObjectMapperProblemRepository(this.url, null));
    }

    @Test
    public void getAllProblemsShouldParseProblemsFromUrlUsingObjectMapper() throws IOException {
        List<Problem> problems = List.of(mock(Problem.class));
        when(this.objectMapper.readValue(eq(this.url), any(CollectionType.class))).thenReturn(problems);

        UrlObjectMapperProblemRepository repository = new UrlObjectMapperProblemRepository(this.url, this.objectMapper);
        assertThat(repository.getAllProblems(), equalTo(problems));
    }

    @Test
    public void getAllProblemsShouldThrowResourceLoadExceptionWhenObjectMapperFails() throws IOException {
        when(this.objectMapper.readValue(eq(this.url), any(CollectionType.class))).thenThrow(IOException.class);
        UrlObjectMapperProblemRepository repository = new UrlObjectMapperProblemRepository(this.url, this.objectMapper);
        assertThrows(ResourceLoadException.class, repository::getAllProblems);
    }

}
