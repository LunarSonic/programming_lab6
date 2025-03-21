package server.commands;
import common.commands.Command;
import common.commands.CommandName;
import common.objects.Organization;
import common.utility.Console;
import common.utility.ExecutionResponse;
import server.managers.CollectionManager;

/**
 * Класс команды update
 */
public class UpdateCommand extends Command {
    private final CollectionManager collectionManager;
    private final Console console;

    /**
     * Конструктор класса UpdateCommand
     */
    public UpdateCommand() {
        super(CommandName.update.name(), "обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = CollectionManager.getCollectionManagerInstance();
        this.console = Console.getConsoleInstance();
    }

    /**
     * Выполняется команда обновления значения элемента коллекции, у которого id равен заданному
     * @param args массив с аргументами команды
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        if (args[1].isEmpty()) {
            return new ExecutionResponse(false, "Неправильное количество аргументов!\n");
        }
        String idString = args[1].trim();
        if (idString.isEmpty()) {
            return new ExecutionResponse(false, "id не может быть пустым!");
        }
        long id;
        try {
            id = Long.parseLong(idString);
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "id должен быть типа long! Получена строка: " + idString);
        }
        Organization newOrganization = Organization.fromArray(args[2].split(","));
        var previous_id = collectionManager.getObjectById(id);
        if (previous_id == null || !collectionManager.getOrganizationCollection().contains(previous_id)) {
            return new ExecutionResponse(false, "Такого ID не существует!");
        }
        console.println("Обновляется организация с ID " + id);
        collectionManager.replaceOrganizationById(id, newOrganization);
        collectionManager.sort();
        return new ExecutionResponse("Элемент обновлён :)");
    }
}
