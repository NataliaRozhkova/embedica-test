package cars.server.handlers;

import cars.data.Response;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public abstract class BaseHandler<T, K> implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        T requestParamValue = null;
        if ("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
        } else if ("POST".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handlePostRequest(httpExchange);
        }
        handleResponse(httpExchange, presentResponse(requestRepository(requestParamValue)));
    }

    abstract T handleGetRequest(HttpExchange httpExchangeParameters);

    abstract T handlePostRequest(HttpExchange httpExchangeParameters) throws IOException;

    abstract Response<K> requestRepository(T requestParameter);

    abstract String presentResponse(Response<K> response);


    private void handleResponse(HttpExchange httpExchange, String response) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(200, response.length());
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }


}
