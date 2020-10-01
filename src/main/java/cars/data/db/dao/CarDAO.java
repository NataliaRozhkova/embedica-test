package cars.data.db.dao;

import cars.data.Response;
import cars.data.entity.Car;
import org.apache.commons.dbcp2.BasicDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarDAO {

    private static final int COLUMN_CAR_NUMBER = 1;
    private static final int COLUMN_CAR_BRAND = 2;
    private static final int COLUMN_CAR_COLOR = 3;
    private static final int COLUMN_YEAR_OF_ISSUE = 4;
    private static final int ID = 1;
    private static final int SUCCESS = 1;
    private static final int ERROR = 0;

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
        } finally {
            if (connection != null) {
                closeConnection(connection);
            }
        }
        return response;
    }

    public Response<List<Car>> readAll() {
        Response<List<Car>> response;
        Connection connection = null;
        try {
            connection = dbSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQLCars.GET_ALL.QUERY);
            ArrayList<Car> cars = mappingToCar(statement.executeQuery());
            response = new Response<>(cars, Response.State.SUCCESS);
        } catch (SQLException exception) {
            response = new Response<>(null, Response.State.ERROR);
        } finally {
            if (connection != null) {
                closeConnection(connection);
            }
        }
        return response;
    }

    public Response<List<Car>> readAll(final HashMap<String, String> parameters) {
        ArrayList<Car> cars;
        Connection connection = null;
        Response<List<Car>> response;
        try {
            connection = dbSource.getConnection();
            PreparedStatement statement = setStatementGet(connection, parameters);
            cars = mappingToCar(statement.executeQuery());
            response = new Response<>(cars, Response.State.SUCCESS);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
            response = new Response<>(null, Response.State.ERROR);
        } finally {
            if (connection != null) {
                closeConnection(connection);
            }
        }
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
                response = new Response<>("Object not found", Response.State.ERROR);
            } else {
                response = new Response<>("Error", Response.State.ERROR);
            }

        } catch (SQLException exception) {
            response = new Response<>("Error", Response.State.ERROR);
        } finally {
            if (connection != null) {
                closeConnection(connection);
            }
        }
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

    private PreparedStatement setStatementGet(final Connection connection, final HashMap<String, String> parameters) throws SQLException {
        return connection.prepareStatement(generateSelectQuery(parameters));
    }

    private String generateSelectQuery(final HashMap<String, String> parameters) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM cars ");
        if (parameters.containsKey("number") || parameters.containsKey("color") || parameters.containsKey("year_of_issue") || parameters.containsKey("brand")) {
            query.append("WHERE ");
        }
        if (parameters.containsKey("number")) {
            query.append("number = '")
                    .append(parameters.get("number"))
                    .append("' ");
        }
        if (parameters.containsKey("color")) {
            if (parameters.containsKey("number")) {
                query.append(" and ");
            }
            query.append("color = '")
                    .append(parameters.get("color"))
                    .append("' ");
        }
        if (parameters.containsKey("year_of_issue")) {
            if (parameters.containsKey("number") || parameters.containsKey("color")) {
                query.append(" and ");
            }
            query.append("year_of_issue = '")
                    .append(parameters.get("year_of_issue"))
                    .append("' ");
        }
        if (parameters.containsKey("brand")) {
            if (parameters.containsKey("number") || parameters.containsKey("color") || parameters.containsKey("year_of_issue")) {
                query.append(" and ");
            }
            query.append("brand = '")
                    .append(parameters.get("brand"))
                    .append("' ");
        }
        if (parameters.containsKey("sort_parameter")) {
            query.append("ORDER BY ")
                    .append(parameters.get("sort_parameter"))
                    .append(" ");
        }
        if (parameters.containsKey("limit")) {
            query.append("LIMIT ")
                    .append(parameters.get("limit"))
                    .append(" ");
        }
        if (parameters.containsKey("offset")) {
            query.append("OFFSET ")
                    .append(parameters.get("offset"))
                    .append(" ");
        }
        return query.append(";").toString();

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

    public enum SQLCars {
        GET_ALL("SELECT * FROM cars"),
        INSERT("INSERT INTO cars (number, brand, color, year_of_issue, date) VALUES ( (?), (?), (?), (?), CURRENT_TIMESTAMP )"),
        DELETE("DELETE FROM cars WHERE id = (?)");

        String QUERY;

        SQLCars(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
