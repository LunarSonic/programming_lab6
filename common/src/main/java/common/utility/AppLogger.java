package common.utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс, который отвечает за логирование в приложении
 * Выводит информационные и ошибочные сообщения
 */
public class AppLogger {
    private final Logger logger;

    /**
     * Конструктор, принимающий класс и создающий логгер для него
     * @param clazz класс, из которого мы вызываем логгер
     */
    public AppLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    /**
     * Метод для вывода дополнительной информации в лог
     * @param message сообщение
     */
    public void info(String message) {
        logger.info(message);
    }

    /**
     * Метод для вывода ошибки в лог
     * @param message сообщение
     */
    public void error(String message) {
        logger.error(message);
    }
}
