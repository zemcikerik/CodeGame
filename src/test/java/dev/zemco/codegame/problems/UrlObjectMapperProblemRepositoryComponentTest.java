package dev.zemco.codegame.problems;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;
import java.util.UUID;

import static dev.zemco.codegame.TestConstants.COMPONENT_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Tag(COMPONENT_TEST)
public class UrlObjectMapperProblemRepositoryComponentTest {

    @Test
    public void repositoryShouldProvideProblemsFromJsonFile() {
        ObjectMapper mapper = new ObjectMapper();
        URL source = UrlObjectMapperProblemRepositoryComponentTest.class.getResource("/problems.json");
        UrlObjectMapperProblemRepository repository = new UrlObjectMapperProblemRepository(source, mapper);

        List<Problem> result = repository.getAllProblems();

        assertThat(result, hasSize(2));

        assertThat(result.get(0).getId(), equalTo(UUID.fromString("92e13b73-c0f2-4db3-9283-38320e943b8c")));
        assertThat(result.get(0).getName(), equalTo("Test Problem 1"));
        assertThat(result.get(0).getDescription(), equalTo("Description"));
        assertThat(result.get(0).getDifficulty(), is(1));
        assertThat(result.get(0).getCases(), hasSize(1));
        assertThat(result.get(0).getCases().get(0).getInputs(), emptyIterable());
        assertThat(result.get(0).getCases().get(0).getExpectedOutputs(), hasSize(1));
        assertThat(result.get(0).getCases().get(0).getExpectedOutputs(), contains(42));
        assertThat(result.get(0).getCases().get(0).getMemorySettings().getSize(), is(1));
        assertThat(result.get(0).getCases().get(0).getMemorySettings().getInitialValues(), anEmptyMap());

        assertThat(result.get(1).getId(), equalTo(UUID.fromString("02e13b73-c0f2-4db3-9283-38320e943b8d")));
        assertThat(result.get(1).getName(), equalTo("Test Problem 2"));
        assertThat(result.get(1).getDescription(), equalTo("other"));
        assertThat(result.get(1).getDifficulty(), is(13));
        assertThat(result.get(1).getCases(), hasSize(1));
        assertThat(result.get(1).getCases().get(0).getInputs(), equalTo(List.of(1, 2, 3)));
        assertThat(result.get(1).getCases().get(0).getExpectedOutputs(), equalTo(List.of(1, 2, 3)));
        assertThat(result.get(1).getCases().get(0).getMemorySettings().getSize(), is(5));
        assertThat(result.get(1).getCases().get(0).getMemorySettings().getInitialValues(), aMapWithSize(1));
        assertThat(result.get(1).getCases().get(0).getMemorySettings().getInitialValues(), hasEntry(2, -100000));
    }

}
