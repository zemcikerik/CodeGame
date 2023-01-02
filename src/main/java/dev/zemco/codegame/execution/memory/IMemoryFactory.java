package dev.zemco.codegame.execution.memory;

/**
 * Produces {@link IMemory memory}.
 *
 * @author Erik Zemčík
 * @see IMemory
 */
public interface IMemoryFactory {

    /**
     * Creates a {@link IMemory memory} with the minimum specified {@code size}.
     *
     * @param size minimum size of the memory
     * @return memory
     * @throws IllegalArgumentException if size is not a positive non-zero integer
     */
    IMemory createMemoryWithSize(int size);

}
