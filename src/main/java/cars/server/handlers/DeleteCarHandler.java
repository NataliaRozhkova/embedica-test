package cars.server.handlers;

import cars.data.Response;
import cars.data.repository.Repository;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.util.HashMap;

public class DeleteCarHandler extends BaseHandler<HashMap<String, String>, String> implements HttpHandler {

    private Repository repository;

    public DeleteCarHandler(Repository repository) {
        this.repository = repository;
    }

    @Override
    HashMap<String, String> handleGetRequest(HttpExchange httpExchangeParameters) {
        String requestParameters = httpExchangeParameters.getRequestURI().getQuery();
        if (requestParameters != null) {
            HashMap<String, String> parameters = new HashMap<>();
            for (String parameter : requestParameters.split("&")) {
                String[] pair = parameter.split("=");
                parameters.put(pair[0], pair[1]);
            }
            return parameters;
        } else return null;
    }

    @Override
    HashMap<String, String> handlePostRequest(HttpExchange httpExchangeParameters)   {
        return null;
    }

    @Override
    Response<String> requestRepository(HashMap<String, String> requestParameter) {
        return repository.delete(getId(requestParameter));
    }

    @Override
    String presentResponse(Response<String> response) {
        return response.body;
    }


    private long getId(final HashMap<String, String> requestParameters) {
        return Long.parseLong(requestParameters.get(ID));
    }


    private String ID = "id";
}
