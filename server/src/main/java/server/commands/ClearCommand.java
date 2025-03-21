package server.commands;
import common.commands.Command;
import common.commands.CommandName;
import common.utility.ExecutionResponse;
import server.managers.CollectionManager;

/**
 * Класс команды clear
 */
public class ClearCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса ClearCommand
     */
    public ClearCommand() {
        super(CommandName.clear.name(), "очистить коллекцию");
        this.collectionManager = CollectionManager.getCollectionManagerInstance();
    }

    /**
     * Выполняется команда очистки коллекции
     * @param args массив с аргументами команды
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        if (args.length != 1)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        collectionManager.clearCollection();
        return new ExecutionResponse("Коллекция была очищена");
    }
}
