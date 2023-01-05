package dev.zemco.codegame.execution.instructions;

public class InstructionExecutionException extends Exception {

    public InstructionExecutionException(String message) {
        super(message);
    }

    public InstructionExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
