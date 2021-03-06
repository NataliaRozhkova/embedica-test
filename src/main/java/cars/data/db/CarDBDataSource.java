package cars.data.db;

import cars.data.Response;
import cars.data.db.dao.CarDAO;
import cars.data.db.dao.StatisticDAO;
import cars.data.entity.Car;
import cars.data.entity.Statistic;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class CarDBDataSource {

    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private static final String USER = "user1";
    private static final String PASSWORD = "123";
    private static final String DB_NAME = "cars";
    private static final String DB_TABLE = "cars";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String TABLE_DESCRIPTION = " (id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
            "number varchar(255) UNIQUE, " +
            "brand varchar(255), " +
            "color varchar(255), " +
            "year_of_issue integer," +
            "date timestamp)";
    private static final int DATABASE_NAME = 1;
    private static final int DB_TABLE_NAME = 3;
    private static final int MIN_NUMBER_IDLE_CONNECTION_IN_POOL = 10;
    private static final int MAX_NUMBER_IDLE_CONNECTION_IN_POOL = 20;


    private final BasicDataSource dataSource;

    public CarDBDataSource() {
        this.dataSource = initDataSource();
    }

    public Response<String> create(final Car car) {
        return new CarDAO(dataSource).create(car);
    }

    public Response<String> delete(final long id) {
        return new CarDAO(dataSource).delete(id);
    }

    public Response<List<Car>> getAll() {
        return new CarDAO(dataSource).readAll();
    }

    public Response<List<Car>> getAll(final HashMap<String, String> parameters) {
        return new CarDAO(dataSource).readAll(parameters);
    }

    public Response<Statistic> getStatistic() {
        return new StatisticDAO(dataSource).read();
    }

    public BasicDataSource getDataSource() {
        return dataSource;
    }

    private BasicDataSource initDataSource() {
        if (this.dataSource != null) {
            return this.dataSource;
        }
        BasicDataSource dataSource = init();
        if (!validationDataBase(dataSource)) {
            return null;
        }
        if (!validationDBTable(dataSource)) {
            if (createDBTable(dataSource)) {
                return dataSource;
            }
        }
        return dataSource;
    }

    private BasicDataSource init() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(URL + DB_NAME);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASSWORD);
        dataSource.setMinIdle(MIN_NUMBER_IDLE_CONNECTION_IN_POOL);
        dataSource.setMaxIdle(MAX_NUMBER_IDLE_CONNECTION_IN_POOL);
        return dataSource;
    }

    private boolean validationDataBase(BasicDataSource dataSource) {
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet set = metaData.getCatalogs();
            while (set.next()) {
                if (set.getString(DATABASE_NAME).equals(DB_NAME)) {
                    return true;
                }
            }
            return false;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private boolean validationDBTable(BasicDataSource dataSource) {
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet set = metaData.getTables(DB_NAME, null, null, new String[]{"TABLE"});
            while (set.next()) {
                if (set.getString(DB_TABLE_NAME).equals(DB_NAME)) {
                    return true;
                }
            }
            return false;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }


    private boolean createDBTable(BasicDataSource dataSource) {
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(CREATE_TABLE + DB_TABLE + TABLE_DESCRIPTION);
            return statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }

    }
}
