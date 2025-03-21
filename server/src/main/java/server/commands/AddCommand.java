package server.commands;
import common.commands.Command;
import common.commands.CommandName;
import common.objects.Organization;
import common.utility.ExecutionResponse;
import server.managers.CollectionManager;

/**
 * Класс команды add
 */
public class AddCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса AddCommand
     */
    public AddCommand() {
        super(CommandName.add.name(), "добавить новый элемент в коллекцию");
        this.collectionManager = CollectionManager.getCollectionManagerInstance();
    }

    /**
     * Выполняется команда добавления нового элемента Organization в коллекцию
     * @param args массив с аргументами команды
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        if (args.length != 2) {
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        }
        Organization organization = Organization.fromArray(args[1].split(","));
        collectionManager.addElement(organization);
        return new ExecutionResponse("Организация успешно добавлена :)");
    }
}