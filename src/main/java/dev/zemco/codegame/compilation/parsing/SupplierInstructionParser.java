package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.Instruction;

import java.util.function.Supplier;

public class SupplierInstructionParser implements InstructionParser {

    private final String instructionName;
    private final Supplier<Instruction> instructionSupplier;

    public SupplierInstructionParser(String instructionName, Supplier<Instruction> instructionSupplier) {
        if (instructionName == null) {
            throw new IllegalArgumentException("Instruction name cannot be null!");
        }

        if (instructionSupplier == null) {
            throw new IllegalArgumentException("Instruction supplier cannot be null!");
        }

        this.instructionName = instructionName;
        this.instructionSupplier = instructionSupplier;
    }

    @Override
    public boolean canParseInstruction(String instruction) {
        return this.instructionName.equals(instruction);
    }

    @Override
    public Instruction parseInstruction(String instruction) {
        return this.instructionSupplier.get();
    }

}
