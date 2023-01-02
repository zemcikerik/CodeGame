package dev.zemco.codegame.execution.memory;

/**
 * Simple {@link IMemoryCell memory cell} that does not hold any value from the start.
 * @author Erik Zemčík
 */
public class SimpleMemoryCell implements IMemoryCell {

    private Integer value;

    /**
     * Creates an instance of {@link SimpleMemoryCell} that holds no value.
     */
    public SimpleMemoryCell() {
        this.value = null;
    }

    @Override
    public boolean hasValue() {
        return this.value != null;
    }

    @Override
    public int getValue() {
        if (!this.hasValue()) {
            throw new IllegalStateException("Cell holds no value!");
        }
        return this.value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

}
