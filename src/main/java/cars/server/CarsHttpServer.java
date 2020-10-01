package cars.server;

import cars.data.repository.Repository;
import cars.server.handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class CarsHttpServer {

    final String host;
    final int port;
    final HttpServer server;
    Repository repository = new Repository();

    public CarsHttpServer(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/add", new PutCarHandler(repository));
        server.createContext("/", new StartServiceHandler());
        server.createContext("/list", new GetCarsHandler(repository));
        server.createContext("/delete", new DeleteCarHandler(repository));
        server.createContext("/stat", new GetStatisticHandler(repository));
        server.setExecutor(Executors.newFixedThreadPool(10));
    }

    public void start() {
        server.start();
    }
}
