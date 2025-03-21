package server;
import common.utility.AppLogger;
import server.managers.CollectionManager;
import server.network.NetworkServer;
import java.net.InetSocketAddress;

/**
 * Класс для запуска серверного приложения
 */
public class App {
    private static final AppLogger logger = new AppLogger(App.class);
    public static void main(String[] args) {
        CollectionManager collectionManager = CollectionManager.getCollectionManagerInstance();
        collectionManager.loadCollection();
        NetworkServer networkServer = new NetworkServer(new InetSocketAddress(8898));
        if (networkServer.init()) {
            networkServer.start();
        } else {
            logger.error("Ошибка при инициализации сервера");
            System.exit(1);
        }
    }
}



