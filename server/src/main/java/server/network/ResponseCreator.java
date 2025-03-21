package server.network;
import common.commands.CommandName;
import common.network.Request;

/**
 * Класс для создания ответа от сервера
 */
public class ResponseCreator {
    private static ResponseCreator instance = null;

    public static ResponseCreator getResponseCreatorInstance() {
        if (instance == null) {
            instance = new ResponseCreator();
        }
        return instance;
    }

    /**
     * Метод, который парсит команду и аргументы из полученного запроса от клиента
     * @param receivedRequest полученный запрос от клиента
     * @return массив строк, содержащий команду и аргументы
     */
    public String[] parseCommand(Request receivedRequest) {
        String[] commandAndArgs;
        if (isCommandWithObjectArg(receivedRequest.getCommandName())) {
            commandAndArgs = new String[]{receivedRequest.getCommandName().toString(), receivedRequest.getCommandObjectArg().toString()};
        } else if (isCommandWithArgs(receivedRequest.getCommandName())) {
            commandAndArgs = new String[]{receivedRequest.getCommandName().toString(), receivedRequest.getCommandArgs()};
        } else if (isCommandWithArgsAndObjectArg(receivedRequest.getCommandName())) {
            String command = receivedRequest.getCommandName().toString() + " " + receivedRequest.getCommandArgs() + " " + receivedRequest.getCommandObjectArg().toString();
            commandAndArgs = command.trim().split(" ", 3);
        } else {
            commandAndArgs = new String[]{receivedRequest.getCommandName().toString()};
        }
        return commandAndArgs;
    }

    /**
     * Метод для проверки на команды с аргументом, прредставляющий собой объект Organization
     * @param command команда
     * @return true, если это команда add, add_if_min или add_if_max, иначе false
     */
    private boolean isCommandWithObjectArg(CommandName command) {
        return command == CommandName.add_if_min || command == CommandName.add_if_max || command == CommandName.add;
    }

    /**
     * Метод для проверки на команды с обычным аргументом
     * @param command команда
     * @return true, если это команда remove_by_id или remove_all_by_annual_turnover, иначе false
     */
    private boolean isCommandWithArgs(CommandName command) {
        return command == CommandName.remove_by_id || command == CommandName.remove_all_by_annual_turnover;
    }

    /**
     * Метод для проверки на команду с обычным аргументом и аргументом, прредставляющим собой объект Organization
     * @param command команда
     * @return true, если это команда update, иначе false
     */
    private boolean isCommandWithArgsAndObjectArg(CommandName command) {
        return command == CommandName.update;
    }
}
