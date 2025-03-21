package common.utility;
import java.io.PrintStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс для ввода команд и вывода результата
 */
public class Console implements Serializable {
    @Serial
    private static final long serialVersionUID = 14L;

    /**
     * Экземпляр класса Console (Singleton)
     */
    private static Console instance = null;

    /**
     * Геттер для экземпляра класса Console,
     * если его нет, то создаётся новый
     * @return instance
     */
    public static Console getConsoleInstance() {
        if (instance == null) {
            instance = new Console();
        }
        return instance;
    }

    private final PrintStream console;

    /**
     * Строка приглашения
     */
    private static final String prompt = "$ ";

    /**
     * Сканер для чтения из файла
     */
    private static Scanner fileScanner = null;

    /**
     * Сканер для чтения из стандартного ввода
     */
    private static final Scanner defScanner = new Scanner(System.in);

    /**
     * Конструктор класса Console
     */
    private Console() {
        this.console = System.out;
    }

    /**
     * Выводит obj.toString() в консоль
     * @param obj объект, который будет выведен
     */
    public void print(Object obj) {
        console.print(obj);
    }

    /**
     * Выводит obj.toString() + \n (перенос строки) в консоль
     * @param obj объект, который будет выведен
     */
    public void println(Object obj) {
        console.println(obj);
    }


    /**
     * Чтение строки из fileScanner или defScanner,
     * с проверкой на наличие данных, чтобы избежать исключений.
     */
    public String readInput() throws IllegalStateException, NoSuchElementException {
        if (fileScanner != null) {
            //чтение строки из файла
            return fileScanner.nextLine();
        } else {
            //чтение строки из консоли
            return defScanner.nextLine();
        }
    }


    /**
     * Проверка, есть ли ещё строки для чтения
     * @return возвращает true, если ещё остались строки для чтения, иначе false
     */
    public boolean hasNextInput() {
        return (fileScanner != null) ? fileScanner.hasNextLine() : defScanner.hasNextLine();
    }


    /**
     * Геттер для получения текущего prompt (приглашения)
     * @return prompt
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Переключает ввод на файл, ввод данных будет из переданного сканера
     * @param scanner сканер для чтения из файла
     */
    public void useFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    /**
     * Переключает ввод на стандартный поток вывода (консоль)
     */
    public void useConsoleScanner() {
        fileScanner = null;
    }
}
