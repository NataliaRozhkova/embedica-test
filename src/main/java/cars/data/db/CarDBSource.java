package cars.data.db;

import cars.data.Response;
import cars.data.db.dao.CarDAO;
import cars.entity.Car;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.List;

public class CarDBSource {

    private final BasicDataSource dataSource;
    private final String url;
    private final String user;
    private final String password;
    private final String dbName;
    private final String dbTableName;


    public CarDBSource(String url, String user, String password, String dbName, String dbTableName) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.dbName = dbName;
        this.dbTableName = dbTableName;
        dataSource = initDataSource();
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

    public Response<List<Car>> getAll(final String sortParam, final int limit, final int offset) {
        return new CarDAO(dataSource).readAll(sortParam, limit, offset);
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
        dataSource.setUrl(url + dbName);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setMinIdle(MIN_NUMBER_IDLE_CONNECTION_IN_POOL);
        dataSource.setMaxIdle(MAX_NUMBER_IDLE_CONNECTION_IN_POOL);
        return dataSource;
    }

    private boolean validationDataBase(BasicDataSource dataSource) {
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet set = metaData.getCatalogs();
            while (set.next()) {
                if (set.getString(DATABASE_NAME).equals(dbName)) {
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
            ResultSet set = metaData.getTables(dbName, null, null, new String[]{"TABLE"});
            while (set.next()) {
                if (set.getString(DB_TABLE_NAME).equals(dbName)) {
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
            PreparedStatement statement = dataSource.getConnection().prepareStatement(CREATE_TABLE + dbTableName + TABLE_DESCRIPTION);
            return statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }

    }

    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private final String TABLE_DESCRIPTION = " (id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY, number varchar(255) UNIQUE, brand varchar(255), color varchar(255), year_of_issue integer)";

    private final int DATABASE_NAME = 1;
    private final int DB_TABLE_NAME = 3;
    private final int MIN_NUMBER_IDLE_CONNECTION_IN_POOL = 10;
    private final int MAX_NUMBER_IDLE_CONNECTION_IN_POOL = 20;


}
