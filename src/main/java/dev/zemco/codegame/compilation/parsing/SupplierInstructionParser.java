package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

import java.util.function.Supplier;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotEmpty;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

public class SupplierInstructionParser implements IInstructionParser {

    private final String instructionName;
    private final Supplier<IInstruction> instructionSupplier;

    public SupplierInstructionParser(String instructionName, Supplier<IInstruction> instructionSupplier) {
        this.instructionName = checkArgumentNotEmpty(instructionName, "Instruction name");
        this.instructionSupplier = checkArgumentNotNull(instructionSupplier, "Instruction supplier");
    }

    @Override
    public boolean canParseInstruction(String instructionLine) {
        return this.instructionName.equals(instructionLine);
    }

    @Override
    public IInstruction parseInstruction(String instructionLine) {
        return this.instructionSupplier.get();
    }

}
