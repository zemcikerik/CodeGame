package dev.zemco.codegame.compilation.parsing;

import dev.zemco.codegame.execution.instructions.IInstruction;

import java.util.Optional;
import java.util.function.Supplier;

import static dev.zemco.codegame.util.Preconditions.checkArgumentNotNull;

/**
 * Parses a named {@link IInstruction instruction} without parameters by requesting it from {@link Supplier supplier}.
 * The {@link Supplier supplier} cannot be modified after construction.
 *
 * @author Erik Zemčík
 * @see Supplier
 */
public class SupplierInstructionParser extends AbstractNamedParametrizedInstructionParser {

    private final Supplier<IInstruction> instructionSupplier;

    /**
     * Creates an instance of {@link SupplierInstructionParser} that parses {@link IInstruction instructions}
     * with the given name by requesting them from the given {@link Supplier supplier}.
     *
     * @param instructionName name of the raw {@link IInstruction instruction}
     * @param instructionSupplier source of parsed {@link IInstruction instructions}
     *
     * @throws IllegalArgumentException if {@code instructionName} is {@code null} or empty or
     *                                  if {@code instructionSupplier} is {@code null}
     */
    public SupplierInstructionParser(String instructionName, Supplier<IInstruction> instructionSupplier) {
        super(instructionName);
        this.instructionSupplier = checkArgumentNotNull(instructionSupplier, "Instruction supplier");
    }

    /**
     * Parses the named {@link IInstruction instruction} by retrieving it from the {@link Supplier supplier}.
     * The {@code parameters} argument must be empty for all {@link Supplier supplier} backed
     * {@link IInstruction instructions}.
     *
     * @param parameters raw instruction parameters
     * @return {@link IInstruction instruction} from the {@link Supplier supplier}
     *
     * @throws IllegalArgumentException if {@code parameters} are {@code null}
     * @throws InstructionParseException if length of {@code parameters} is not zero
     */
    @Override
    protected Optional<IInstruction> parseInstructionFromParameters(String[] parameters) {
        this.checkParameterFixedCount(parameters, 0);
        return Optional.of(this.instructionSupplier.get());
    }

}
