package dev.zemco.codegame.compilation;

import dev.zemco.codegame.execution.instructions.Instruction;

import java.util.List;

// TODO: interface?
public class Program {

    private final List<Instruction> instructions;

    public Program(List<Instruction> instructions) {
        // TODO: checks
        this.instructions = instructions;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

}
