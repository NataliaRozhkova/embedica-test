package cars.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class StartServiceHandler implements HttpHandler {

    String htmlPage = getFile();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        handleResponse(exchange);
    }

    private void handleResponse(HttpExchange httpExchange) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(200, htmlPage.length());

        outputStream.write(htmlPage.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }

    private static String getFile() {
        InputStream is = StartServiceHandler.class.getClassLoader().getResourceAsStream("ex.html");
        Scanner scanner = new Scanner(is, "UTF-8");
        StringBuilder result = new StringBuilder("");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            result.append(line).append("\n");
        }
        scanner.close();
        return result.toString();
    }

}