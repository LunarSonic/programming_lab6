package client;
import client.cli.Runner;
import client.network.NetworkClient;
import client.utility.CommandHandler;

/**
 * Класс для запуска клиентского приложения
 */
public class App {
    public static void main(String[] args) {
        //var networkClient = new NetworkClient("helios.cs.ifmo.ru", 8898);
        var networkClient = new NetworkClient("localhost", 8898);
        networkClient.connect();
        var commandHandler = new CommandHandler(networkClient);
        new Runner(commandHandler).interactiveMode();
    }
}

