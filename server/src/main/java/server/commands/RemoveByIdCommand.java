package server.commands;
import common.commands.Command;
import common.commands.CommandName;
import common.objects.Organization;
import common.utility.ExecutionResponse;
import server.managers.CollectionManager;

/**
 * Класс команды remove_by_id
 */
public class RemoveByIdCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса RemoveByIdCommand
     */
    public RemoveByIdCommand() {
        super(CommandName.remove_by_id.name(), "удалить элемент из коллекции по его id");
        this.collectionManager = CollectionManager.getCollectionManagerInstance();
    }

    /**
     * Выполняется команда удаления элемента Organization из коллекции по id
     * @param args массив с аргументами команды
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        if (args[1].isEmpty() || args.length != 2)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        long id;
        try {
            id = Long.parseLong(args[1].trim());
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "id не был распознан");
        }
        Organization organization = collectionManager.getObjectById(id);
        if (organization == null) {
            return new ExecutionResponse(false, "Такого id не существует");
        }
        collectionManager.removeByIdFromCollection(id);
        return new ExecutionResponse("Элемент коллекции c id: " + id + " был удалён");
    }
}

