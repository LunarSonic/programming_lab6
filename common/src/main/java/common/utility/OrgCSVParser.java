package common.utility;
import common.objects.Address;
import common.objects.Coordinates;
import common.objects.Organization;
import common.objects.OrganizationType;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Класс для преобразования объекта Organization в CSV-строку и обратно
 */
public class OrgCSVParser implements Serializable {
    @Serial
    private static final long serialVersionUID = 17L;

    /**
     * Метод, который преобразует строковый массив из CSV в объект Organization
     * @param args массив с аргументами команды
     * @param logger для вывода ошибки
     * @return объект Organization
     */
    public static Organization fromArray(String[] args, AppLogger logger) {
        try {
            Long id = Long.parseLong(args[0]);
            String name = args[1];
            Float x = Float.parseFloat(args[2]);
            Long y = Long.parseLong(args[3]);
            ZonedDateTime creationDate = ZonedDateTime.parse(args[4], DateTimeFormatter.ISO_ZONED_DATE_TIME);
            long annualTurnover = Long.parseLong(args[5]);
            OrganizationType type = (args[6].equals("null") || args[6].isEmpty()) ? null : OrganizationType.valueOf(args[6]);
            Address postalAddress = new Address(args[7]);
            Coordinates coordinates = new Coordinates(x, y);
            return new Organization(id, name, coordinates, creationDate, annualTurnover, type, postalAddress);
        } catch (Exception e) {
            logger.error("Ошибка при создании объекта Organization");
            return null;
        }
    }

    /**
     * Метод, преобразующий объект Organization в строковый массив CSV
     * @param organization объект, который будет преобразован
     * @return массив строк
     */
    public static String[] toArray(Organization organization) {
        var list = new ArrayList<String>();
        list.add(Long.toString(organization.getId()));
        list.add(organization.getName());
        list.add(Float.toString(organization.getCoordinates().getX()));
        list.add(Long.toString(organization.getCoordinates().getY()));
        list.add(organization.getCreationDate().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        list.add(Long.toString(organization.getAnnualTurnover()));
        list.add(organization.getType() != null ? organization.getType().toString() : "null");
        list.add(organization.getPostalAddress() != null ? organization.getPostalAddress().getStreet() : "null");
        return list.toArray(new String[0]);
    }
}
