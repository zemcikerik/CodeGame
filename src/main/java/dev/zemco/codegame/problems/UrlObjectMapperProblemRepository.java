package dev.zemco.codegame.problems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import dev.zemco.codegame.resources.ResourceLoadException;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * {@link Problem} repository which uses {@link URL} as a constant data source
 * and {@link ObjectMapper} for deserialization.
 *
 * @author Erik Zemčík
 * @see ObjectMapper
 */
public class UrlObjectMapperProblemRepository implements IProblemRepository {

    private final URL url;
    private final ObjectMapper objectMapper;
    private final CollectionType problemListType;

    /**
     * Creates an instance of {@link UrlObjectMapperProblemRepository}.
     *
     * @param url resource to use as a constant data source
     * @param objectMapper object mapper to use for deserialization
     *
     * @throws IllegalArgumentException if {@code url} is {@code null} or if {@code objectMapper} is {@code null}
     */
    public UrlObjectMapperProblemRepository(URL url, ObjectMapper objectMapper) {
        this.url = checkArgumentNotNull(url, "Url");
        this.objectMapper = checkArgumentNotNull(objectMapper, "Object mapper");

        TypeFactory typeFactory = this.objectMapper.getTypeFactory();
        this.problemListType = typeFactory.constructCollectionType(List.class, Problem.class);
    }

    /**
     * Retrieves and deserializes all problems from the {@link URL} datasource.
     * @return unmodifiable {@link List list} of problems
     * @throws ResourceLoadException if retrieval or deserialization fails
     */
    @Override
    public List<Problem> getAllProblems() {
        try {
            List<Problem> problems = this.objectMapper.readValue(this.url, this.problemListType);
            return Collections.unmodifiableList(problems);
        } catch (IOException e) {
            throw new ResourceLoadException("Failed to deserialize problems from URL!", e);
        }
    }

}
