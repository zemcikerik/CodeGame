package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static dev.zemco.codegame.TestConstants.UNIT_TEST;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag(UNIT_TEST)
public class DelegatingInstructionParserTest {

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionIfInstructionParserListIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new DelegatingInstructionParser(null));
    }

    @Test
    public void parseInstructionShouldThrowIllegalArgumentExceptionIfRawInstructionIsNull() {
        DelegatingInstructionParser parser = new DelegatingInstructionParser(emptyList());
        assertThrows(IllegalArgumentException.class, () -> parser.parseInstruction(null));
    }

    @Test
    public void parseInstructionShouldThrowIllegalArgumentExceptionIfRawInstructionIsEmpty() {
        DelegatingInstructionParser parser = new DelegatingInstructionParser(emptyList());
        assertThrows(IllegalArgumentException.class, () -> parser.parseInstruction(""));
    }

    @Test
    public void parseInstructionShouldReturnEmptyOptionalIfNoParserIsCapableOfParsingInstruction() {
        DelegatingInstructionParser parser = new DelegatingInstructionParser(List.of(this.mockIncapableParser()));
        Optional<IInstruction> result = parser.parseInstruction("unknown");
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void parseInstructionShouldReturnOptionalWithParsedInstructionFromFirstCapableParser() {
        Optional<IInstruction> parsedInstruction = Optional.of(mock(IInstruction.class));
        IInstructionParser capableParser = mock(IInstructionParser.class);
        when(capableParser.parseInstruction(anyString())).thenReturn(parsedInstruction);

        DelegatingInstructionParser parser = new DelegatingInstructionParser(List.of(
            this.mockIncapableParser(), capableParser, capableParser
        ));

        Optional<IInstruction> result = parser.parseInstruction("known");

        assertThat(result, equalTo(parsedInstruction));
        verify(capableParser, times(1)).parseInstruction("known");
    }

    private IInstructionParser mockIncapableParser() {
        IInstructionParser parser = mock(IInstructionParser.class);
        when(parser.parseInstruction(anyString())).thenReturn(Optional.empty());
        return parser;
    }

}
