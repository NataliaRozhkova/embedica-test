package cars.server.handlers;

import cars.data.Response;
import cars.data.entity.CarJSONConverter;
import cars.data.repository.Repository;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;

public class PutCarHandler extends BaseHandler<String, String> implements HttpHandler {

    private final Repository repository;

    public PutCarHandler(Repository repository) {
        this.repository = repository;
    }

    @Override
    String handlePostRequest(HttpExchange httpExchange) throws IOException {
        InputStream stream = httpExchange.getRequestBody();
        StringBuilder json = new StringBuilder();
        int jsonByte;
        while ((jsonByte = stream.read()) != -1) {
            json.append((char) jsonByte);
        }
        stream.close();
        return json.toString();
    }

    @Override
    Response<String> requestRepository(String requestParameter) {
        return repository.put(CarJSONConverter.deserialize(requestParameter));
    }

    @Override
    String presentResponse(Response<String> response) {
        return response.body;
    }

    @Override
    String handleGetRequest(HttpExchange httpExchange) {
        return null;
    }

}
