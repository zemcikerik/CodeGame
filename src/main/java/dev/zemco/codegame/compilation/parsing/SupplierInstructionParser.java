package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

import java.util.function.Supplier;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;
import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNullAndNotEmpty;

public class SupplierInstructionParser implements IInstructionParser {

    private final String instructionName;
    private final Supplier<IInstruction> instructionSupplier;

    public SupplierInstructionParser(String instructionName, Supplier<IInstruction> instructionSupplier) {
        this.instructionName = checkArgumentNotNullAndNotEmpty(instructionName, "Instruction name");
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
