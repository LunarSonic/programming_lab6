package common.forms;
import common.utility.AppLogger;
import common.objects.Coordinates;
import common.exceptions.FormBreak;
import common.exceptions.NotInLimitsException;
import common.utility.Console;

import java.io.Serial;
import java.util.NoSuchElementException;

/**
 * Класс для формирования координат
 */
public class CoordinatesForm extends BasicFormation<Coordinates> {
    @Serial
    private static final long serialVersionUID = 5L;
    private final Console console;
    private final AppLogger logger;

    public CoordinatesForm() {
        this.console = Console.getConsoleInstance();
        this.logger = new AppLogger(CoordinatesForm.class);
    }

    @Override
    public Coordinates form() throws FormBreak {
        Float x = askX();
        Long y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Метод, который запрашивает координату X
     * @return значение координаты X
     */
    private Float askX() throws FormBreak {
        float x;
        while (true) {
            try {
                console.println("Введите координату X (тип Float): ");
                String line = console.readInput().trim();
                if (line.equals("exit")) throw new FormBreak();
                if (!line.isEmpty()) {
                    x = Float.parseFloat(line);
                    if (x <= -947) throw new NotInLimitsException();
                    break;
                } else {
                    logger.error("Поле не может быть null");
                }
            } catch (NotInLimitsException e) {
                logger.error("Значение должно быть больше -947");
            } catch (NumberFormatException e) {
                logger.error("Значение должно быть типа Float");
            } catch (NoSuchElementException e) {
                logger.error("Данное значение поля не может быть использовано");
            } catch (IllegalStateException e) {
                logger.error("Непредвиденная ошибка");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Метод, который запрашивает координату Y
     * @return значение координаты Y
     */
    private Long askY() throws FormBreak {
        long y;
        while (true) {
            try {
                console.println("Введите координату Y (тип Long): ");
                String line = console.readInput().trim();
                if (line.equals("exit")) throw new FormBreak();
                if (!line.isEmpty()) {
                    y = Long.parseLong(line);
                    break;
                } else {
                    logger.error("Поле не может быть null");
                }
            } catch (NumberFormatException e) {
                logger.error("Значение должно быть типа Long");
            } catch (IllegalStateException e) {
                logger.error("Непредвиденная ошибка");
                System.exit(0);
            }
        }
        return y;
    }
}
