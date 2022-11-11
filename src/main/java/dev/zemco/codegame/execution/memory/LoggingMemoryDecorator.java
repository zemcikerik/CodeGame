package dev.zemco.codegame.execution.memory;

// TODO: either remove me or add proper logger
public class LoggingMemoryDecorator implements IMemory {

    private final IMemory memory;

    public LoggingMemoryDecorator(IMemory memory) {
        this.memory = memory;
    }

    @Override
    public IMemoryCell getCellByAddress(int address) {
        System.out.format("[Memory] Retrieving memory at address %d!", address);
        return this.memory.getCellByAddress(address);
    }

    @Override
    public IMemoryCell getWorkingCell() {
        System.out.println("[Memory] Retrieving working cell!");
        return this.memory.getWorkingCell();
    }

}
