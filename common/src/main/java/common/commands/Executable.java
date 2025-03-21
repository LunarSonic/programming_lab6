package common.commands;
import common.utility.ExecutionResponse;

/**
 * Интерфейс для выполнения команд
 */
public interface Executable {
    ExecutionResponse execute(String[] args);
}
