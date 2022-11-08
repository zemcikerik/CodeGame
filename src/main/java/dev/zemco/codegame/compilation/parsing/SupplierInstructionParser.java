package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.Instruction;

import java.util.function.Supplier;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNullAndNotEmpty;

public class SupplierInstructionParser implements InstructionParser {

    private final String instructionName;
    private final Supplier<Instruction> instructionSupplier;

    public SupplierInstructionParser(String instructionName, Supplier<Instruction> instructionSupplier) {
        this.instructionName = checkArgumentNotNullAndNotEmpty(instructionName, "Instruction name");
        this.instructionSupplier = checkArgumentNotNull(instructionSupplier, "Instruction supplier");
    }

    @Override
    public boolean canParseInstruction(String instructionLine) {
        return this.instructionName.equals(instructionLine);
    }

    @Override
    public Instruction parseInstruction(String instructionLine) {
        return this.instructionSupplier.get();
    }

}
