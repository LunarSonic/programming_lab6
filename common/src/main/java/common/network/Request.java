package common.network;
import common.commands.CommandName;
import java.io.Serial;
import java.io.Serializable;

/**
 * Класс, представляющий собой запрос с командой и аргументами
 */
public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 9L;
    private final CommandName commandName;
    private final String commandArgs;
    private final Object commandObjectArg;

    /**
     * Конструктор класса Request
     * @param commandName название команды (экзмепляр Enum'а)
     * @param commandArgs аргумент команды (id или число)
     * @param commandObjectArg аргумент команды, представляющий собой объект Organization
     */
    public Request(CommandName commandName, String commandArgs, Object commandObjectArg) {
        this.commandName = commandName;
        this.commandArgs = commandArgs;
        this.commandObjectArg = commandObjectArg;
    }

    /**
     * Конструктор класса Request c аргументами команды
     * @param commandName название команды (экзмепляр Enum'а)
     * @param commandArgs аргумент команды (id или число)
     */
    public Request(CommandName commandName, String commandArgs) {
        this(commandName, commandArgs, null);
    }

    public CommandName getCommandName() {
        return commandName;
    }

    public String getCommandArgs() {
        return commandArgs;
    }

    public Object getCommandObjectArg() {
        return commandObjectArg;
    }
}
