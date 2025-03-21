package server.commands;
import common.commands.Command;
import common.commands.CommandName;
import common.utility.ExecutionResponse;
import server.managers.CollectionManager;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс команды info
 */
public class InfoCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса InfoCommand
     */
    public InfoCommand() {
        super(CommandName.info.name(), "вывести в стандартный поток вывода информацию о коллекции");
        this.collectionManager = CollectionManager.getCollectionManagerInstance();
    }

    /**
     * Выполняется команда вывода информации о коллекции
     * @param args массив с аргументами команды
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        if (args.length != 1)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        ZonedDateTime lastInitTime = collectionManager.getLastInitTime();
        String lastInitTimeString = (lastInitTime == null) ? "в этой сессии ещё не было инициалиазции" : lastInitTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        ZonedDateTime lastSaveTime = collectionManager.getLastSaveTime();
        String lastSaveTimeString = (lastSaveTime == null) ? "в этой сессии ещё не было сохранения" : lastSaveTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        return new ExecutionResponse("Информация о коллекции: " + "\n" + "Тип: " + collectionManager.getOrganizationCollection().getClass() + "\n" +
                "Кол-во элементов: " + collectionManager.getOrganizationCollection().size() + "\n" + "Дата последней инициалиазции: " + lastInitTimeString + "\n" +
                "Дата последнего сохранения: " + lastSaveTimeString);

    }
}