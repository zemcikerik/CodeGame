package dev.zemco.codegame.presentation.solution;

import org.fxmisc.richtext.model.StyleSpans;

import java.util.Collection;

public interface IHighlightStyleComputer {
    StyleSpans<Collection<String>> computeHighlightStyles(String text);
}
