package dev.zemco.codegame.programs;

/**
 * Produces {@link IProgramBuilder program builders}.
 *
 * @author Erik Zemčík
 * @see IProgramBuilder
 */
public interface IProgramBuilderFactory {

    /**
     * Creates a {@link IProgramBuilder program builder} ready for use.
     * @return {@link IProgramBuilder program builder}
     */
    IProgramBuilder createProgramBuilder();

}
