package dev.zemco.codegame.presentation.highlighting;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static java.util.List.of;

/**
 * Implementation of a compiler, which computes CSS highlight styles for individual parts of a source code
 * using the CodeGame syntax. Each source code part is treated as an instruction line.
 * Highlighted instructions are specified during the construction of the computer, and are unmodifiable.
 * <p>
 * In CodeGame syntax:
 * <ul>
 *     <li>comments are highlighted using the {@code comment} class</li>
 *     <li>labels are highlighted using the {@code label} class</li>
 *     <li>instruction names are highlighted using the {@code instruction} class</li>
 *     <li>for blank sections no classes are used</li>
 * </ul>
 * For more information about the used syntax see {@link dev.zemco.codegame.compilation.CodeProgramCompiler}.
 *
 * @author Erik Zemčík
 * @see dev.zemco.codegame.compilation.CodeProgramCompiler
 */
public class CodeHighlightStyleComputer implements IHighlightStyleComputer {

    private static final String COMMENT_GROUP_NAME = "comment";
    private static final String LABEL_GROUP_NAME = "label";
    private static final String INSTRUCTION_GROUP_NAME = "instruction";

    private static final Map<String, Collection<String>> GROUP_NAME_TO_STYLES_MAP = Map.of(
        COMMENT_GROUP_NAME, of("comment"),
        LABEL_GROUP_NAME, of("jump-label"),
        INSTRUCTION_GROUP_NAME, of("instruction")
    );

    private static final Collection<String> DEFAULT_STYLES = Collections.emptyList();

    private final Pattern codeHighlightPattern;

    /**
     * Creates an instance of {@link CodeHighlightStyleComputer} with specific instruction names to highlight.
     * @param instructionNames specific instruction names to highlight
     * @throws IllegalArgumentException if {@code instructionNames} is {@code null}
     */
    public CodeHighlightStyleComputer(Set<String> instructionNames) {
        checkArgumentNotNull(instructionNames, "Instruction names");

        String instructionNamesPattern = this.createPatternFromInstructionNames(instructionNames);

        this.codeHighlightPattern = Pattern.compile(
            // captures comment = semicolon, followed by any character until the end of string
            // NOTE: this group has to go first in order to avoid matching of other possible objects in the future
            "(?<" + COMMENT_GROUP_NAME + ">;.*$)" + "|" +

            // captures label = positive lookahead matching start of the string or any whitespace character,
            // followed by >, followed by any non-whitespace character
            // NOTE: positive lookahead is used to prevent invalid characters before the label,
            //       as word boundary \b does not capture words starting with non-word character >
            "(?<" + LABEL_GROUP_NAME + ">(?<=^|\\s)>\\S+)" + "|" +

            // captures instruction = word boundary, followed by any instruction name, ending with word boundary
            "(?<" + INSTRUCTION_GROUP_NAME + ">\\b(" + instructionNamesPattern + "))\\b"
        );
    }

    private String createPatternFromInstructionNames(Set<String> instructionNames) {
        return instructionNames.stream()
            .map(Pattern::quote) // escape characters that would break the pattern
            .collect(Collectors.joining("|"));
    }

    // this method was partially taken and refactored from
    // https://github.com/FXMisc/RichTextFX/blob/5f70d254ab44889cfdcf4451a8c2e18483ec33ac/richtextfx-demos/src/main/java/org/fxmisc/richtext/demo/JavaKeywordsDemo.java#L150-L171
    @Override
    public StyleSpans<Collection<String>> computeHighlightStyles(String text) {
        checkArgumentNotNull(text, "Text");

        // NOTE: StyleSpansBuilder doesn't add zero-length spans, so we don't need to handle those edge cases
        StyleSpansBuilder<Collection<String>> builder = new StyleSpansBuilder<>();

        int lastEndIndex = 0;
        Matcher matcher = this.codeHighlightPattern.matcher(text);

        while (matcher.find()) {
            // add default styles to text between matches (that was not included in any match)
            builder.add(DEFAULT_STYLES, matcher.start() - lastEndIndex);

            Collection<String> styles = this.getCssStylesByMatchedGroupName(matcher);
            builder.add(styles, matcher.end() - matcher.start());

            lastEndIndex = matcher.end();
        }

        // add default styles to text after last match
        builder.add(DEFAULT_STYLES, text.length() - lastEndIndex);
        return builder.create();
    }

    private Collection<String> getCssStylesByMatchedGroupName(Matcher matcher) {
        return GROUP_NAME_TO_STYLES_MAP.entrySet().stream()
            .filter(entry -> matcher.group(entry.getKey()) != null)
            .findAny()
            .map(Map.Entry::getValue)
            .orElseThrow(() -> new IllegalStateException("Unknown group captured by highlight pattern!"));
    }

}
