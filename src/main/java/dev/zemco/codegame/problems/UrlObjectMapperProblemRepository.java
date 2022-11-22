package dev.zemco.codegame.problems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import dev.zemco.codegame.presentation.ResourceLoadException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class UrlObjectMapperProblemRepository implements IProblemRepository {

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
        try {
            return this.objectMapper.readValue(this.url, this.problemListType);
        } catch (IOException e) {
            throw new ResourceLoadException("Failed to deserialize problems from URL!", e);
        }
    }

}
