package dev.zemco.codegame.execution.memory;

// TODO: either remove me or add proper logger
public class LoggingMemoryDecorator implements Memory {

    private final Memory memory;

    public LoggingMemoryDecorator(Memory memory) {
        this.memory = memory;
    }

    @Override
    public MemoryCell getCellByAddress(int address) {
        System.out.format("[Memory] Retrieving memory at address %d!", address);
        return this.memory.getCellByAddress(address);
    }

    @Override
    public MemoryCell getWorkingCell() {
        System.out.println("[Memory] Retrieving working cell!");
        return this.memory.getWorkingCell();
    }

}
