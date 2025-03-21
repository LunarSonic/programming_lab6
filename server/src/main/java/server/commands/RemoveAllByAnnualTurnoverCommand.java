package server.commands;
import common.commands.Command;
import common.commands.CommandName;
import common.utility.ExecutionResponse;
import server.managers.CollectionManager;

/**
 * Класс команды remove_all_by_annual_turnover
 */
public class RemoveAllByAnnualTurnoverCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса RemoveAllByAnnualTurnover
     */
    public RemoveAllByAnnualTurnoverCommand() {
        super(CommandName.remove_all_by_annual_turnover.name(), "удалить из коллекции все элементы, значение поля annualTurnover которого эквивалентно заданному");
        this.collectionManager = CollectionManager.getCollectionManagerInstance();
    }

    /**
     * Выполняется команда удаления всех элементов из коллекции, у которых annualTurnover равен заданному
     * @param args массив с аргументами команды
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        if (args.length != 2)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        long annualTurnover;
        try {
            annualTurnover = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "annualTurnover не был распознан");
        }
        collectionManager.removeAllByAnnualTurnover(annualTurnover);
        return new ExecutionResponse("Элементы коллекции с annualTurnover " + annualTurnover + " были удалены");
    }
}

