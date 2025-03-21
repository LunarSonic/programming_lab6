package client.utility;
import common.commands.CommandName;
import common.objects.Organization;
import common.forms.OrganizationForm;
import common.network.Request;
import common.utility.AppLogger;

/**
 * Класс отвечает за создание запросов на основе названий команд и клиентского ввода
 */
public class RequestCreator {
    private final AppLogger logger = new AppLogger(RequestCreator.class);

    /**
     * Метод, который создаёт запрос для дальнейшей сериализации и отправки на сервер
     * @param command команда
     * @param userCommand  массив строк, представляющих из себя команду и её аргументы
     * @return объект класса Request
     */
    public Request createRequest(CommandName command, String[] userCommand) {
        switch (command) {
            case add, add_if_max, add_if_min -> {
                return createRequestWithOrg(command);
            }
            case remove_by_id, remove_all_by_annual_turnover -> {
                return createRequestWithArg(command, userCommand);
            }
            case update -> {
                return createRequestWithIdAndOrg(command, userCommand);
            }
            default -> {
                return new Request(command, "");
            }
        }
    }

    /**
     * Метод для создания запроса с обычным аргументом (long id или число типа long)
     * @param command команда
     * @param userCommand массив строк, представляющих из себя команду и её аргументы
     * @return объект класса Request
     */
    private Request createRequestWithArg(CommandName command, String[] userCommand) {
        Long num = parseLongFromCommand(userCommand);
        if (num == null) {
            logger.error("Аргумент команды должен быть типа long");
            return null;
        }
        return new Request(command, String.valueOf(num), null);
    }

    /**
     * Метод для создания запроса с аргументом, представляющим собой объект Organization
     * @param command команда
     * @return объект класса Request
     */
    private Request createRequestWithOrg(CommandName command) {
        OrganizationForm organizationForm = new OrganizationForm();
        Organization organization = organizationForm.form();
        String organizationData = organization.toStr();
        return new Request(command, null, organizationData);
    }

    /**
     * Метод для создания запроса с аргументом, представляющим собой объект Organization
     * @param command команда
     * @param userCommand массив строк, представляющих из себя команду и её аргументы
     * @return объект класса Request
     */
    private Request createRequestWithIdAndOrg(CommandName command, String[] userCommand) {
        if (userCommand.length < 2) {
            logger.error("Отсутствует id");
            return null;
        }
        Long num = parseLongFromCommand(userCommand);
        if (num == null) {
            logger.error("Аргумент команды должен быть типа long");
            return null;
        }
        OrganizationForm organizationForm = new OrganizationForm();
        Organization organization = organizationForm.form();
        String organizationData = organization.toStr();
        return new Request(command, String.valueOf(num), organizationData);
    }

    /**
     * Парсинг аргумента у команды (он должен быть типа long)
     * @param userCommand массив строк, представляющих из себя команду и её аргументы
     * @return объект класса Request
     */
    private Long parseLongFromCommand(String[] userCommand) {
        try {
            return Long.parseLong(userCommand[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
