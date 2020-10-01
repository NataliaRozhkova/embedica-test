package cars.server.handlers;

import cars.data.Response;
import cars.data.repository.Repository;
import cars.data.entity.Statistic;
import cars.data.entity.StatisticJSONConverter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GetStatisticHandler implements HttpHandler {

    private final Repository repository;

    public GetStatisticHandler(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        handleResponse(httpExchange);
    }

    private void handleResponse(HttpExchange httpExchange) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        String statistic = presentResponse(requestRepository());
        httpExchange.sendResponseHeaders(200, statistic.length());

        outputStream.write(statistic.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    String presentResponse(Response<Statistic> response) {
        return StatisticJSONConverter.convertToJSON(response.body);
    }

    private Response<Statistic> requestRepository() {
        return repository.getStatistic();
    }
}
