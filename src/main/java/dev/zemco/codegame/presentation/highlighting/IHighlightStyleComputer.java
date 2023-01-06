package dev.zemco.codegame.presentation.highlighting;

import org.fxmisc.richtext.model.StyleSpans;

import java.util.Collection;

/**
 * Computes CSS highlight styles for individual parts of a source code for the library RichTextFX.
 * These styles affect how the source code is displayed to the user.
 * Syntax of the source code is dependent on the implementation.
 *
 * @author Erik Zemčík
 */
public interface IHighlightStyleComputer {

    /**
     * Computes CSS highlight styles for a given part of the source code.
     * These styles are divided into spans, where each span contains styles for the given subpart of the source code.
     *
     * @param text part of the source code
     * @return style class spans
     *
     * @throws IllegalArgumentException if {@code text} is {@code null}
     */
    StyleSpans<Collection<String>> computeHighlightStyles(String text);

}
