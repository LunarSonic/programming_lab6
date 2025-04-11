package client.network;
import common.network.Request;
import common.utility.ExecutionResponse;
import common.utility.Console;
import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Класс, отвечающий за сетевое взаимодействие с сервером
 */
public class NetworkClient {
    private final String host;
    private final int port;
    private SocketChannel channel;
    private final Console console = Console.getConsoleInstance();

    /**
     * Конструктор класса NetworkClient
     * @param host хост сервера
     * @param port порт сервера
     */
    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Метод для подключения к серверу
     */
    public void connect() {
        try {
            SocketAddress address = new InetSocketAddress(host, port);
            channel = SocketChannel.open();
            channel.connect(address);
            console.println("Соединение с сервером установлено");
        } catch (ConnectException e) {
            console.println("Сервер недоступен, пвторите попытку позже");
            System.exit(1);
        } catch (IOException e) {
            console.println("Ошибка подключения к серверу");
            System.exit(1);
        }
    }

    /**
     * Метод для отправки запроса на сервер и получения ответа от него
     * @param request запрос с командой и аргументами
     * @return response ответ от сервера (результат выполнения команды)
     */
    public ExecutionResponse sendAndReceive(Request request) {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(bytes)) {
            outputStream.writeObject(request);
            ByteBuffer data = ByteBuffer.wrap(bytes.toByteArray());
            channel.write(data);

            ByteBuffer receivedDataLength = ByteBuffer.allocate(4);
            channel.read(receivedDataLength );
            receivedDataLength .flip();
            int responseLength = receivedDataLength.getInt();
            ByteBuffer dataToReceive = ByteBuffer.allocate(responseLength);
            channel.read(dataToReceive);
            dataToReceive.flip(); //переводим буфер в режим чтения

            try (ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(dataToReceive.array()))) {
                ExecutionResponse response = (ExecutionResponse) inputStream.readObject();
                console.println(response.getMessage());
                return response;
            } catch (ClassNotFoundException e) {
                return new ExecutionResponse(false, "Ошибка десериализации ответа от сервера");
            }
        } catch (IOException e) {
            return new ExecutionResponse(false, "Ошибка в связи с сервером");
        }
    }
}
