package server.commands;
import common.commands.Command;
import common.commands.CommandName;
import common.utility.ExecutionResponse;
import server.managers.CollectionManager;

/**
 * Класс команды show
 */
public class ShowCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса ShowCommand
     */
    public ShowCommand() {
        super(CommandName.show.name(), "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = CollectionManager.getCollectionManagerInstance();
    }

    /**
     * Выполняется команда вывода всех элементов коллекции в строковом виде
     * @param args массив с аргументами команды
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        if (args.length != 1)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        String collectionData = collectionManager.toString();
        if(collectionData.isEmpty()) {
            return new ExecutionResponse(false, "Коллекция пуста!");
        } else {
            return new ExecutionResponse(collectionData);
        }
    }
}