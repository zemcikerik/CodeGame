package dev.zemco.codegame.problems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class UrlObjectMapperProblemRepository implements ProblemRepository {

    private final URL url;
    private final ObjectMapper objectMapper;
    private final CollectionType problemListType;

    public UrlObjectMapperProblemRepository(URL url, ObjectMapper objectMapper) {
        this.url = checkArgumentNotNull(url, "Url");
        this.objectMapper = checkArgumentNotNull(objectMapper, "Object mapper");
        this.problemListType = this.objectMapper.getTypeFactory().constructCollectionType(List.class, Problem.class);
    }

    @Override
    public List<Problem> getAllProblems() {
        // TODO: proper exception
        try {
            return this.objectMapper.readValue(this.url, this.problemListType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
