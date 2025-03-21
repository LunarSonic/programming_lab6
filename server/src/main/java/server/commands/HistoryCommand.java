package server.commands;
import common.commands.Command;
import common.commands.CommandName;
import common.utility.ExecutionResponse;
import server.managers.CommandManager;

/**
 * Класс команды history для вывода последних 15 команд
 */
public class HistoryCommand extends Command {
    private final CommandManager commandManager;

    /**
     * Конструктор класса HistoryCommand
     * @param commandManager менеджер команд
     */
    public HistoryCommand(CommandManager commandManager) {
        super(CommandName.history.name(), "вывести последние 15 команд (без их аргументов)");
        this.commandManager = commandManager;
        command = CommandName.history;
    }

    /**
     * Выполняется команда вывода истории команд
     * @param args массив с аргументами команды
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        if (args.length != 1)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        if (commandManager.getCommandHistory() == null) {
            return new ExecutionResponse(false, "История пустая!");
        }
        StringBuilder historyOutput = new StringBuilder("Введённые команды: \n");
        for (String command : commandManager.getCommandHistory()) {
            historyOutput.append(command).append("\n");
        }
        return new ExecutionResponse(historyOutput.toString());
    }
}

