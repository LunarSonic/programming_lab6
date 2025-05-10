package common.objects;
import common.utility.Model;
import java.io.Serial;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Главный класс организации
 */
public class Organization extends Model {
    @Serial
    private static final long serialVersionUID = 12L;
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private final java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final long annualTurnover; //Значение поля должно быть больше 0
    private final OrganizationType type; //Поле может быть null
    private final Address postalAddress; //Поле не может быть null

    public Organization(Long id, String name, Coordinates coordinates, ZonedDateTime creationDate, long annualTurnover, OrganizationType type, Address postalAddress) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.annualTurnover = annualTurnover;
        this.type = type;
        this.postalAddress = postalAddress;
    }

    /**
     * Метод для преобразования объекта Organization в строку
     * @return представление в виде строки
     */
    public String toStr() {
        return String.join(",",
                Long.toString(this.id),
                this.name,
                this.coordinates.toString(),
                this.creationDate.toString(),
                Long.toString(this.annualTurnover),
                this.type != null ? this.type.toString() : "",
                this.postalAddress != null ? this.postalAddress.getStreet() : ""
        );
    }

    /**
     * Метод для создания объекта Organization из массива данных
     * @param data массив данных
     * @return новый объект Organization
     */
    public static Organization fromArray(String[] data) {
        return new Organization(
                Long.parseLong(data[0]),
                data[1],
                new Coordinates(Float.parseFloat(data[2]), Long.parseLong(data[3])),
                ZonedDateTime.parse(data[4]),
                Long.parseLong(data[5]),
                data[6].isEmpty() ? null : OrganizationType.valueOf(data[6]),
                data[7].isEmpty() ? null : new Address(data[7])
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public long getAnnualTurnover() {
        return annualTurnover;
    }

    public OrganizationType getType() {
        return type;
    }

    public Address getPostalAddress() {
        return postalAddress;
    }

    @Override
    public boolean validate() {
        if(id <= 0) return false;
        if(name == null || name.isEmpty()) return false;
        if(coordinates == null) return false;
        if(creationDate == null) return false;
        if(annualTurnover <= 0 ) return false;
        return postalAddress != null;
    }

    /**
     * Метод для сравнения 2 объектов (по годовому обороту)
     * @param model объект для сравнения
     * @return annualTurnover
     */
    @Override
    public int compareTo(Model model) {
        return Long.compare(this.annualTurnover, model.getAnnualTurnover());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, annualTurnover, type, postalAddress);
    }

    @Override
    public String toString() {
        return "Организация \"" + getName() + "\", id: " + getId() + "\n" +
                "Координаты: {x: " + getCoordinates().getX() + ", y: " + getCoordinates().getY() + "}\n" +
                "Дата создания: " + getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss")) + "\n" +
                "Годовой оборот: " + getAnnualTurnover() + "\n" +
                "Тип организации: " + (getType() != null ? getType() : "не указан") + "\n" +
                "Адрес: " + getPostalAddress().getStreet();
    }
}
