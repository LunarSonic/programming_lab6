package server.commands;
import common.commands.Command;
import common.commands.CommandName;
import common.objects.Organization;
import common.utility.ExecutionResponse;
import server.managers.CollectionManager;

/**
 * Класс команды add_if_min
 */
public class AddIfMinCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса AddIfMinCommand
     */
    public AddIfMinCommand() {
        super(CommandName.add_if_min.name(), "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
        this.collectionManager = CollectionManager.getCollectionManagerInstance();
    }

    /**
     * Выполняется команда добавления нового элемента Organization в коллекцию, если его значение annualTurnover
     * меньше min значения, которое есть в коллекции
     * @param args массив с аргументами команды
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        if (args.length != 2)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        Organization organization = Organization.fromArray(args[1].split(","));
        boolean added = collectionManager.addIfMin(organization);
        if (!added) {
            return new ExecutionResponse(false, "Организация не была добавлена!");
        }
        return new ExecutionResponse("Минимальный элемент добавлен в коллекцию :)");
    }
}

