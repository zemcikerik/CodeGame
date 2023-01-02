package dev.zemco.codegame.execution.memory;

/**
 * Produces {@link IMemoryCell memory cells}.
 *
 * @author Erik Zemčík
 * @see IMemoryCell
 */
public interface IMemoryCellFactory {

    /**
     * Creates a {@link IMemoryCell memory cell} ready for a use.
     * @return memory cell
     */
    IMemoryCell createMemoryCell();

}
