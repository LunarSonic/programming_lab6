package client.commands;
import common.commands.Command;
import common.commands.CommandName;
import common.utility.ExecutionResponse;
import java.util.Map;

/**
 * Класс команды help
 */
public class HelpCommand extends Command {
    private final Map<CommandName, String[]> commands;

    public HelpCommand(Map<CommandName, String[]> commands) {
        super(CommandName.help.name(), "вывести справку по доступным командам");
        this.commands = commands;
    }

    /**
     * Выполняется команда вывода справки по всем доступным командам
     * @param args массив с аргументами команды
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        if (args.length > 2)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        StringBuilder helpMessage = new StringBuilder("Справка по доступным командам:\n");
        commands.values().forEach(command ->
                helpMessage.append(String.format(" %-35s%-1s%n", command[0], command[1])));
        return new ExecutionResponse(helpMessage.toString());
    }
}
