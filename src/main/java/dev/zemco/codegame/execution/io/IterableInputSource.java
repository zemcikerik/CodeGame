package dev.zemco.codegame.execution.io;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Input source that uses an {@link Iterable<Integer>} as source of inputs.
 * @author Erik Zemčík
 */
public class IterableInputSource implements InputSource {

    private final Iterator<Integer> iterator;

    /**
     * Creates an instance of {@link IterableInputSource} by wrapping an {@link Iterable<Integer>}.
     * @param iterable iterable to use as source of inputs
     * @throws IllegalArgumentException if iterable is null
     */
    public IterableInputSource(Iterable<Integer> iterable) {
        if (iterable == null) {
            throw new IllegalArgumentException("Iterable cannot be null!");
        }
        this.iterator = iterable.iterator();
    }

    // TODO: docs here?

    @Override
    public boolean hasNextValue() {
        return this.iterator.hasNext();
    }

    @Override
    public int getNextValue() {
        if (!this.hasNextValue()) {
            throw new NoSuchElementException();
        }
        return this.iterator.next();
    }

}