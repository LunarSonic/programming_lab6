package common.managers;
import common.utility.AppLogger;
import com.opencsv.CSVReader;
import common.objects.Organization;
import common.utility.Console;
import common.utility.OrgCSVParser;
import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс, отвечающий за работу с файлами
 */
public class FileManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 8L;
    private final Set<String> allFilePaths;
    private final String csvFilePath;
    private final AppLogger logger;
    private final Console console;
    private static FileManager instance;

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    /**
     * Конструктор класса FileManager
     */
    public FileManager() {
        this.console = Console.getConsoleInstance();
        this.logger = new AppLogger(FileManager.class);
        this.allFilePaths = loadFilesFromEnvironmentVariables();
        if (this.allFilePaths == null) {
            logger.error("Не удалось загрузить пути к файлам, проверьте LAB6_PATH!");
        }
        this.csvFilePath = findOrgCSVFilePath();
    }

    /**
     * Метод, который возвращает список файлов, полученных из переменной окружения LAB5_PATH
     * @return filePaths список из всех путей
     */
    public Set<String> loadFilesFromEnvironmentVariables() {
        String lab6Path = System.getenv("LAB6_PATH");
        if (lab6Path == null) {
            logger.error("Переменная окружения LAB6_PATH не установлена :(");
            return null;
        }
        Set<String> filePaths = Arrays.stream(lab6Path.split(File.pathSeparator))
                .map(String::trim)
                .filter(path -> new File(path).isFile())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        console.println("Файлы из переменной окружения: ");
        filePaths.forEach(console::println);
        return filePaths;
    }

    /**
     * Метод, который ищет среди загруженных путей первый CSV файл
     * @return csvFilePath
     */
    public String findOrgCSVFilePath() {
        if (allFilePaths == null) {
            return null;
        }
        return allFilePaths.stream()
                .filter(path -> path.endsWith(".csv"))
                .findFirst()
                .orElse(null);
    }

    /**
     * Метод, который ищет скрипт по названию файла
     * @param fileName название файла со скриптом
     * @return путь к файлу со скриптом
     */
    public String findScriptFilePath(String fileName) {
        return allFilePaths.stream()
                .filter(path -> path.endsWith(fileName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Геттер для получения пути к CSV файлу
     * @return csvFilePath
     */
    public String getOrgCSVFilePath() {
        return csvFilePath;
    }

    /**
     * Метод, который записывает коллекцию в файл
     * @param collection коллекция, которую мы записываем в CSV
     */
    public void writeCollectionToCSV(Collection<Organization> collection) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(csvFilePath))) {
            StringBuilder csvString = new StringBuilder();
            csvString.append("id,name,coordinates.x,coordinates.y,creationDate,annualTurnover,type,postalAddress\n");
            for (Organization organization : collection) {
                String[] organizationData = OrgCSVParser.toArray(organization);
                csvString.append(String.join(",", organizationData)).append("\n");
            }
            outputStream.write(csvString.toString().getBytes());
        } catch (IOException e) {
            logger.error("Не удалось записать коллекцию в CSV");
        }
    }

    /**
     * Метод, который преобразовывает CSV-строку в коллекцию
     * @param csvString CSV-строка
     * @return коллекция
     */
    private LinkedHashSet<Organization> convertCSVToCollection(String csvString) {
        try {
            StringReader stringReader = new StringReader(csvString);
            CSVReader csvReader = new CSVReader(stringReader);
            LinkedHashSet<Organization> collection = new LinkedHashSet<>();
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                Organization organization = OrgCSVParser.fromArray(record, logger);
                if (organization != null && organization.validate()) {
                    collection.add(organization);
                } else {
                    logger.error("Ошибка валидации организации: " + Arrays.toString(record));
                }
            }
            csvReader.close();
            return collection;
        } catch (Exception e) {
            logger.error("Ошибка при десериализации данных из CSV");
            return null;
        }
    }

    /**
     * Метод, который считывает коллекцию из файла
     * @param collection считанная коллекция
     */
    public void readCollectionFromCSV(Collection<Organization> collection) {
        if (csvFilePath == null) {
            logger.error("Путь уть к CSV файлу не установлен, проверьте переменную окружения LAB6_PATH!");
            return;
        }
        logger.info("Происходит чтение файла: " + getOrgCSVFilePath());
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(csvFilePath))) {
            BufferedReader bufferedReader =  new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            boolean isFirstLine = true;
            while ((line = bufferedReader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                stringBuilder.append(line).append("\n");
            }
            LinkedHashSet<Organization> organizations = convertCSVToCollection(stringBuilder.toString());
            collection.clear();
            if (organizations != null) {
                collection.addAll(organizations);
                if (!collection.isEmpty()) {
                    logger.info("Коллекция успешно загружена из файла");
                } else {
                    logger.error("В файле не найдена подходящая коллекция");
                }
            } else {
                logger.error("Ошибка при загрузке коллекции из CSV");
            }
        } catch (FileNotFoundException e) {
            logger.error("Файл не был найден");
        } catch (IOException e) {
            logger.error("Ошибка при чтении из файла");
        }
    }
}
