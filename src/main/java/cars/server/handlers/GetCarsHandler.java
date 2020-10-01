package cars.server.handlers;

import cars.data.Response;
import cars.data.entity.Car;
import cars.data.entity.CarJSONConverter;
import cars.data.repository.Repository;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.HashMap;
import java.util.List;

public class GetCarsHandler extends BaseHandler<HashMap<String, String>, List<Car>> implements HttpHandler {

    private static final String SORT_PARAMETER = "sort_parameter";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final Repository repository;

    public GetCarsHandler(Repository repository) {
        this.repository = repository;
    }

    @Override
    HashMap<String, String> handleGetRequest(HttpExchange httpExchange) {
        String httpExchangeParameters = httpExchange.getRequestURI().getQuery();
        if (httpExchangeParameters != null) {
            HashMap<String, String> parameters = new HashMap<>();
            for (String parameter : httpExchangeParameters.split("&")) {
                String[] pair = parameter.split("=");
                parameters.put(pair[0], pair[1]);
            }
            return parameters;
        } else return null;
    }

    @Override
    HashMap<String, String> handlePostRequest(HttpExchange httpExchangeParameters) {
        return null;
    }

    @Override
    Response<List<Car>> requestRepository(HashMap<String, String> requestParameter) {
        if (requestParameter != null) {
            return repository.getAll(
                    requestParameter.get(SORT_PARAMETER),
                    Integer.parseInt(requestParameter.get(LIMIT)),
                    Integer.parseInt(requestParameter.get(OFFSET)));
        } else {
            return (repository.getAll());
        }
    }

    @Override
    String presentResponse(Response<List<Car>> response) {
        return CarJSONConverter.convertToJSON(response.body);
    }
}