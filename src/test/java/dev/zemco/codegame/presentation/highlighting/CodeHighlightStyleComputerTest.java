package dev.zemco.codegame.presentation.highlighting;

import org.fxmisc.richtext.model.StyleSpans;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CodeHighlightStyleComputerTest {

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfInstructionNameSetIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new CodeHighlightStyleComputer(null));
    }

    @Test
    public void computeHighlightStylesShouldThrowIllegalArgumentExceptionIfInstructionLineIsNull() {
        CodeHighlightStyleComputer highlightStyleComputer = new CodeHighlightStyleComputer(emptySet());
        assertThrows(IllegalArgumentException.class, () -> highlightStyleComputer.computeHighlightStyles(null));
    }

    @Test
    public void computeHighlightStylesShouldReturnEmptyStyleSpansWhenInstructionLineIsEmpty() {
        CodeHighlightStyleComputer highlightStyleComputer = new CodeHighlightStyleComputer(emptySet());
        StyleSpans<Collection<String>> result = highlightStyleComputer.computeHighlightStyles("");
        assertThat(result.length(), is(0));
    }

    @Test
    public void computeHighlightStylesShouldReturnDefaultStyles() {
        CodeHighlightStyleComputer highlightStyleComputer = new CodeHighlightStyleComputer(emptySet());
        StyleSpans<Collection<String>> result = highlightStyleComputer.computeHighlightStyles("Hello World!");
        assertThat(result.getSpanCount(), is(1));
    }

    @Test
    public void computeHighlightStylesShouldHighlightInstructionNames() {
        Set<String> instructionNames = Set.of("first", "third");
        CodeHighlightStyleComputer highlightStyleComputer = new CodeHighlightStyleComputer(instructionNames);

        StyleSpans<Collection<String>> result = highlightStyleComputer.computeHighlightStyles("first second third");

        assertThat(result.getSpanCount(), is(3));
        assertThat(result.getStyleSpan(0).getLength(), is("first".length()));
        assertThat(result.getStyleSpan(1).getLength(), is(" second ".length()));
        assertThat(result.getStyleSpan(2).getLength(), is("third".length()));
    }

    @Test
    public void computeHighlightStylesShouldHighlightInstructionNamesContainingCharactersInvalidForRegex() {
        Set<String> instructionNames = Set.of("hello\\world");
        CodeHighlightStyleComputer highlightStyleComputer = new CodeHighlightStyleComputer(instructionNames);

        StyleSpans<Collection<String>> result = highlightStyleComputer.computeHighlightStyles("test hello\\world");

        assertThat(result.getSpanCount(), is(2));
        assertThat(result.getStyleSpan(0).getLength(), is("test ".length()));
        assertThat(result.getStyleSpan(1).getLength(), is("hello\\world".length()));
    }

    @Test
    public void computeHighlightStylesShouldHighlightComments() {
        CodeHighlightStyleComputer highlightStyleComputer = new CodeHighlightStyleComputer(emptySet());

        StyleSpans<Collection<String>> result = highlightStyleComputer.computeHighlightStyles("ignored;comment");

        assertThat(result.getSpanCount(), is(2));
        assertThat(result.getStyleSpan(0).getLength(), is("ignored".length()));
        assertThat(result.getStyleSpan(1).getLength(), is(";comment".length()));
    }

    @Test
    public void computeHighlightStylesShouldHighlightJumpLabels() {
        CodeHighlightStyleComputer highlightStyleComputer = new CodeHighlightStyleComputer(emptySet());

        StyleSpans<Collection<String>> result = highlightStyleComputer.computeHighlightStyles("  >label");

        assertThat(result.getSpanCount(), is(2));
        assertThat(result.getStyleSpan(0).getLength(), is("  ".length()));
        assertThat(result.getStyleSpan(1).getLength(), is(">label".length()));
    }

}
