package cars;

import cars.server.CarsHttpServer;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        CarsHttpServer server = new CarsHttpServer("localhost", 8001);
        server.start();
    }
}
