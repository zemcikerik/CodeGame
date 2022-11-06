package dev.zemco.codegame.execution.memory;

public class SimpleMemoryCell implements MemoryCell {

    private Integer value;

    public SimpleMemoryCell() {
        this.value = null;
    }

    @Override
    public boolean hasValue() {
        return this.value != null;
    }

    @Override
    public int getValue() {
        if (this.value == null) {
            throw new IllegalStateException("Cell holds no value!");
        }
        return this.value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

}
