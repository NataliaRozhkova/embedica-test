package cars.data.db.dao;

import cars.data.Response;
import cars.entity.Car;
import org.apache.commons.dbcp2.BasicDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    @NotNull
    private final BasicDataSource dbSource;

    public CarDAO(@NotNull BasicDataSource dataSource) {
        this.dbSource = dataSource;
    }


    public Response<String> create(@NotNull final Car car) {
        Connection connection = null;
        Response<String> response = null;
        try {
            connection = dbSource.getConnection();
            PreparedStatement statement = setStatementPutCar(connection, car);
            if (statement.executeUpdate() == SUCCESS) {
                response = new Response<>("Success", Response.State.SUCCESS);
            }
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 0) {
                response = new Response<>("Object already exists", Response.State.ERROR);
            } else {
                response = new Response<>("Error", Response.State.ERROR);
            }
        }
        closeConnection(connection);
        return response;
    }

    public Response<List<Car>> readAll() {
        ArrayList<Car> cars;
        Response<List<Car>> response;
        Connection connection = null;
        try {
            connection = dbSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQLCars.GET_ALL.QUERY);
            cars = mappingToCar(statement.executeQuery());
            response = new Response<>(cars, Response.State.SUCCESS);
        } catch (SQLException exception) {
            response = new Response<>(null, Response.State.ERROR);

        }
        closeConnection(connection);

        return response;
    }

    public Response<List<Car>> readAll(final String sortParameter, final int limit, final int offset) {
        ArrayList<Car> cars;
        Connection connection = null;
        Response<List<Car>> response;
        try {
            connection = dbSource.getConnection();
            PreparedStatement statement = setStatementGet(connection, sortParameter, limit, offset);
            cars = mappingToCar(statement.executeQuery());
            response = new Response<>(cars, Response.State.SUCCESS);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
            response = new Response<>(null, Response.State.SUCCESS);
        }
        closeConnection(connection);
        return response;
    }

    public Response<String> delete(@NotNull Long carId) {
        Connection connection = null;
        Response<String> response;
        try {
            connection = dbSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQLCars.DELETE.QUERY);
            statement.setLong(ID, carId);
            int result = statement.executeUpdate();
            if (result == SUCCESS) {
                response = new Response<>("Success", Response.State.SUCCESS);
            } else if (result == ERROR) {
                response = new Response<>("Object not found", Response.State.SUCCESS);
            } else {
                response = new Response<>("Error", Response.State.ERROR);
            }

        } catch (SQLException exception) {
            response = new Response<>("Error", Response.State.ERROR);
        }
        closeConnection(connection);
        return response;
    }

    private PreparedStatement setStatementPutCar(final Connection connection, final Car car) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQLCars.INSERT.QUERY);
        statement.setString(COLUMN_CAR_NUMBER, car.number);
        statement.setString(COLUMN_CAR_BRAND, car.brand);
        statement.setString(COLUMN_CAR_COLOR, car.color);
        statement.setInt(COLUMN_YEAR_OF_ISSUE, car.yearOfIssue);
        return statement;
    }

    private PreparedStatement setStatementGet(final Connection connection, final String sortParameter, final int limit, final int offset) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(selectSortingMethod(sortParameter).QUERY);
        statement.setInt(LIMIT, limit);
        statement.setInt(OFFSET, offset);
        return statement;
    }

    private SQLCars selectSortingMethod(final String sortParameter) {
        switch (sortParameter) {
            case "id": {
                return SQLCars.GET_GET_WITH_ORDER_ID;
            }
            case "brand": {
                return SQLCars.GET_GET_WITH_ORDER_BRAND;
            }
            case "color": {
                return SQLCars.GET_WITH_ORDER_COLOR;
            }
            case "year_of_issue": {
                return SQLCars.GET_GET_WITH_ORDER_YEAR;
            }
            case "number": {
                return SQLCars.GET_GET_WITH_ORDER_NUMBER;
            }
            default:
                return SQLCars.GET;
        }
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private ArrayList<Car> mappingToCar(ResultSet set) throws SQLException {
        ArrayList<Car> cars = new ArrayList<>();
        while (set.next()) {
            cars.add(new Car(set.getInt("id"),
                    set.getString("number"),
                    set.getString("brand"),
                    set.getString("color"),
                    set.getInt("year_of_issue")));
        }
        return cars;
    }

    private final int COLUMN_CAR_NUMBER = 1;
    private final int COLUMN_CAR_BRAND = 2;
    private final int COLUMN_CAR_COLOR = 3;
    private final int COLUMN_YEAR_OF_ISSUE = 4;
    private final int LIMIT = 1;
    private final int OFFSET = 2;
    private final int ID = 1;
    private int SUCCESS = 1;
    private int ERROR = 0;


    public enum SQLCars {
        GET_ALL("SELECT * FROM cars"),
        GET_WITH_ORDER_COLOR("SELECT * FROM cars ORDER BY color LIMIT (?) OFFSET (?)"),
        GET_GET_WITH_ORDER_ID("SELECT * FROM cars ORDER BY id LIMIT (?) OFFSET (?)"),
        GET_GET_WITH_ORDER_NUMBER("SELECT * FROM cars ORDER BY number LIMIT (?) OFFSET (?)"),
        GET_GET_WITH_ORDER_BRAND("SELECT * FROM cars ORDER BY brand LIMIT (?) OFFSET (?)"),
        GET_GET_WITH_ORDER_YEAR("SELECT * FROM cars ORDER BY year_of_issue LIMIT (?) OFFSET (?)"),
        GET("SELECT * FROM cars ORDER BY (?) LIMIT (?) OFFSET (?)"),
        INSERT("INSERT INTO cars (number, brand, color, year_of_issue) VALUES ( (?), (?), (?), (?))"),
        DELETE("DELETE FROM cars WHERE id = (?)");

        String QUERY;

        SQLCars(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
