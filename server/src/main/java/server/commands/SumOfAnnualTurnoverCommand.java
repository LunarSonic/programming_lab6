package server.commands;
import common.commands.Command;
import common.commands.CommandName;
import common.objects.Organization;
import common.utility.ExecutionResponse;
import server.managers.CollectionManager;

/**
 * Класс команды sum_of_annualTurnover
 */
public class SumOfAnnualTurnoverCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса SumOfAnnualTurnoverCommand
     */
    public SumOfAnnualTurnoverCommand() {
        super(CommandName.sum_of_annual_turnover.name(), "вывести сумму значений поля annualTurnover для всех элементов коллекции");
        this.collectionManager = CollectionManager.getCollectionManagerInstance();
    }

    /**
     * Выполняется команда вывода суммы всех значений annualTurnover
     * @param args массив с аргументами команды
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        if (args.length != 1)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        long sum = 0;
        for (Organization org : collectionManager.getOrganizationCollection()) {
            sum += org.getAnnualTurnover();
        }
        return new ExecutionResponse("Сумма годового оборота у всех оранизаций: " + sum);
    }
}