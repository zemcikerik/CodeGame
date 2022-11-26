package dev.zemco.codegame.execution.io;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * {@link IInputSource Input source} that uses an {@link Iterable<Integer>} as source of inputs.
 * @author Erik Zemčík
 */
public class IterableInputSource implements IInputSource {

    private final Iterator<Integer> iterator;

    /**
     * Creates an instance of {@link IterableInputSource} by wrapping an {@link Iterable<Integer>}.
     * @param iterable iterable to use as source of inputs
     * @throws IllegalArgumentException if iterable is null
     */
    public IterableInputSource(Iterable<Integer> iterable) {
        this.iterator = checkArgumentNotNull(iterable, "Iterable").iterator();
    }

    /**
     * Returns if the underlying {@link Iterator iterator} has next value.
     * @return true if iterator has next value, else false
     */
    @Override
    public boolean hasNextValue() {
        return this.iterator.hasNext();
    }

    /**
     * Returns next value from the underlying {@link Iterator iterator}.
     *
     * @return next value from iterator
     * @throws NoSuchElementException if iterator has no next value
     */
    @Override
    public int getNextValue() {
        if (!this.hasNextValue()) {
            throw new NoSuchElementException("Iterator has no next value!");
        }
        return this.iterator.next();
    }

}
