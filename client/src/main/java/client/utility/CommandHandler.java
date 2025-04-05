package client.utility;
import client.network.NetworkClient;
import client.network.RequestCreator;
import common.commands.CommandName;
import common.network.Request;
import common.utility.ExecutionResponse;

/**
 * Класс, предназначенный для отправки запроса от клиента на сервер
 */
public class CommandHandler {
    private final NetworkClient networkClient;
    private final RequestCreator requestCreator;

    public CommandHandler(NetworkClient networkClient) {
        this.networkClient = networkClient;
        this.requestCreator = new RequestCreator();
    }

    /**
     * Обработка команды клиента, создание запроса и его отправка на сервер
     * @param command команда для выполнения
     * @param userCommand аргументы команды
     * @return результат выполнения команды от сервера
     */
    public ExecutionResponse handleCommand(CommandName command, String[] userCommand) {
        if (userCommand.length == 0 || userCommand[0].isEmpty()) {
            return new ExecutionResponse(false, "Команда не может быть пустой");
        }
        Request request = requestCreator.createRequest(command, userCommand);
        if (request == null) {
            return new ExecutionResponse(false, "Неверные аргументы у команды!");
        }
        return networkClient.sendAndReceive(request);
    }
}
