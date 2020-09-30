package cars.server.handlers;

import cars.data.Response;
import cars.data.repository.Repository;
import cars.entity.Statistic;
import cars.entity.StatisticJSONConverter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class GetStatisticHandler implements HttpHandler {

    private Repository repository;

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

        outputStream.write(statistic.getBytes("UTF-8"));
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
