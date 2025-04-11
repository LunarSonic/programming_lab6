package server.network;
import common.network.Request;
import common.network.Serializer;
import common.utility.AppLogger;
import common.utility.ExecutionResponse;
import server.managers.CollectionManager;
import server.managers.CommandManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Класс, который принимает подключение от клиента,
 * читает, обрабатывает полученные запросы и отправляет ответы клиенту
 */
public class NetworkServer {
    private final InetSocketAddress address;
    private Selector selector;
    private final CommandManager commandManager;
    private final AppLogger logger = new AppLogger(NetworkServer.class);
    private final RequestParser requestParser;
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса NetworkServer
     * @param address адрес (порт), на котором будет запущен сервер
     */
    public NetworkServer(InetSocketAddress address) {
        this.address = address;
        this.commandManager = CommandManager.getCommandManagerInstance();
        this.requestParser = RequestParser.getRequestParserInstance();
        this.collectionManager = CollectionManager.getCollectionManagerInstance();
    }

    /**
     * Метод, который инициализирует сервер, создаёт канал, регистрируя его в селекторе
     * @return true, если сервер инициализирован, иначе false
     */
    public boolean init() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(address);
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("Сервер инициализирован, ждём подключения клиента ...");
            return true;
        } catch (IOException e) {
            logger.error("Ошибка при инициализации сервера");
            return false;
        }
    }

    /**
     * Метод, который запускает сервер и ожидает подключения клиента
     */
    public void start() {
        //создаём отдельный поток для ввода команд save и exit
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                if (input == null || input.isEmpty()) {
                    continue;
                }
                input = input.trim();
                if (input.equals("save")) {
                    collectionManager.saveCollection();
                    logger.info("Коллекция сохранилась в CSV файл");
                } else if (input.equals("exit")) {
                    logger.info("Завершение работы сервера и сохранение коллекции в CSV файл");
                    collectionManager.saveCollection();
                    System.exit(0);
                } else {
                    logger.error("Сервер не может выполнить данную команду: " + input);
                }
            }
        }).start();
        try {
            while (true) {
                selector.select(); //блокируем выполнение до появления доступных каналов
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            acceptClient(key); //обрабатываем новое подключение
                        } else if (key.isReadable()) {
                            handleClientRequest(key); //обрабатываем запросы от клиента
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка в обработке подключений");
        }
    }

    /**
     * Метод, который принимает подключение клиента
     * @param key объект, полученный из SelectionKey и содержащий данные для регистрации канала
     */
    private void acceptClient(SelectionKey key) throws IOException {
        var serverSocketChannel = (ServerSocketChannel) key.channel();
        var client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        logger.info("Клиент подключился");
    }

    /**
     * Метод, который обрабатывает запрос клиента и отправляет ответ клиенту через канал
     * @param key объект, полученный из SelectionKey и содержащий данные для регистрации канала
     */
    private void handleClientRequest(SelectionKey key) throws IOException {
        var client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(2000);
        int bytesRead = client.read(buffer);
        if (bytesRead == -1) {
            logger.info("Клиент отключился");
            client.close();
            key.cancel(); //отмена регистрации канала
        }
        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        //десериализация полученного запроса от клиента
        Request receivedRequest = Serializer.getInstance().deserialize(data, Request.class);
        if (receivedRequest == null) {
            logger.error("Сервер получил некорректные данные!");
        }
        String requestFromClient = receivedRequest.getCommandName().toString() + " " + (receivedRequest.getCommandArgs() != null ? receivedRequest.getCommandArgs() : "");
        if (receivedRequest.getCommandObjectArg() != null) {
            requestFromClient += " " + receivedRequest.getCommandObjectArg().toString();
        }
        logger.info("Получен запрос: " + requestFromClient);
        var command = commandManager.getCommands().get(receivedRequest.getCommandName().toString());
        if (command == null) {
            sendResponse(client, new ExecutionResponse("Команда не найдена: " + receivedRequest.getCommandName()));
        }

        //парсим аргументы команды
        String[] commandAndArgs = requestParser.parseCommand(receivedRequest);
        ExecutionResponse response = command.execute(commandAndArgs);
        sendResponse(client, response);
        commandManager.addCommandToHistory(receivedRequest.getCommandName().getName());
    }

    /**
     * Метод, который отправляет ответ клиенту
     * @param client канал, в который отправляется ответ
     * @param response ответ от сервера
     */
    private void sendResponse(SocketChannel client, ExecutionResponse response) throws IOException {
        byte[] responseData = Serializer.getInstance().serialize(response);
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        lengthBuffer.putInt(responseData.length);
        lengthBuffer.flip();
        client.write(lengthBuffer);
        ByteBuffer buffer = ByteBuffer.wrap(responseData);
        client.write(buffer);
        logger.info("Ответ отправлен клиенту :)");
    }
}

