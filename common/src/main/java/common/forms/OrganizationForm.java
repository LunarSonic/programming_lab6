package common.forms;
import common.utility.AppLogger;
import common.objects.Address;
import common.objects.Coordinates;
import common.objects.Organization;
import common.objects.OrganizationType;
import common.exceptions.FormBreak;
import common.exceptions.NotInLimitsException;
import common.utility.Console;
import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

/**
 * Класс для формирования организации
 */
public class OrganizationForm extends BasicFormation<Organization> {
    @Serial
    private static final long serialVersionUID = 6L;
    private final Console console;
    private final AppLogger logger;

    public OrganizationForm() {
        this.console = Console.getConsoleInstance();
        this.logger = new AppLogger(OrganizationForm.class);
    }

    @Override
    public Organization form()  {
        Organization organization = null;
        try {
            organization = new Organization(1L,
                    askName(), askCoordinates(),
                    ZonedDateTime.now(), askAnnualTurnover(),
                    askOrganizationType(), askAddress()
            );
        } catch (FormBreak e) {
            logger.error("Создание организации отменено");
        }
        return organization;
    }

    /**
     * Метод, который запращивает у пользователя название организации
     * @return name
     */
    private String askName() throws FormBreak {
        String name;
        while (true) {
            try {
                console.println("Введите название организации: ");
                name = console.readInput().trim();
                if (name.equals("exit")) throw new FormBreak();

                if (name.isEmpty()) {
                    logger.error("Поле не может быть пустым!");
                } else if (!name.matches("^[a-zA-Zа-яА-Я]+(?:'?[a-zA-Zа-яА-Я]+)*(?:\\s[a-zA-Zа-яА-Я]+(?:[a-zA-Zа-яА-Я]+)*)*$")) {
                    logger.error("Название организации может содержать только буквы, пробелы и 1 кавычку!");
                } else {
                    break;
                }
            } catch (IllegalStateException e) {
                logger.error("Непредвиденная ошибка");
                System.exit(0);
            } catch (NoSuchElementException e) {
                logger.error("Данное значение поля не может быть использовано");
            }
        }
        return name;
    }

    /**
     * Метод, который запрашивает координаты x и y
     * @return coordinates
     */
    private Coordinates askCoordinates() throws FormBreak {
        return new CoordinatesForm().form();
    }

    /**
     * Метод, который запрашивает годовой оборот организации
     * @return annualTurnover
     */
    private long askAnnualTurnover() throws FormBreak {
        long annualTurnover;
        while (true) {
            try {
                console.println("Введите годовой оборот: ");
                String line = console.readInput().trim();
                if (line.equals("exit")) throw new FormBreak();
                if(!line.isEmpty()) {
                    annualTurnover = Long.parseLong(line);
                    if(annualTurnover <= 0) throw new NotInLimitsException();
                    break;
                }
            } catch (NotInLimitsException e) {
                logger.error("Значение поля должно быть больше 0");
            } catch (NumberFormatException e) {
                logger.error("Значение поля должно быть типа long");
            } catch (NoSuchElementException e) {
                logger.error("Значение поля не может быть использовано");
            } catch (IllegalStateException e) {
                logger.error("Непредвиденная ошибка");
                System.exit(0);
            }
        }
        return annualTurnover;
    }

    /**
     * Метод, который запрашивает тип организации
     * @return organizationType
     */
    private OrganizationType askOrganizationType() throws FormBreak {
        return new OrganizationTypeForm().form();
    }

    /**
     * Метод, который запрашивает адрес
     * @return address
     */
    private Address askAddress(){
        return new AddressForm().form();
    }
}
