package cars.data.db.dao;

import cars.data.Response;
import cars.entity.Statistic;
import org.apache.commons.dbcp2.BasicDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticDAO {

    @NotNull
    private final BasicDataSource dbSource;

    public StatisticDAO(@NotNull BasicDataSource dataSource) {
        this.dbSource = dataSource;
    }

    public Response<Statistic> read() {
        Statistic statistic = new Statistic(getCount(), getDateFirstEntryAdded(), getDateLastEntryAdded());
        Response.State state = Response.State.ERROR;
        if (statistic.numberOfRecords != 0 || statistic.dateFirstEntryWasAdded != null || statistic.dateLastEntryWasAdded != null) {
            state = Response.State.SUCCESS;
        }
        return new Response<>(statistic, state);
    }

    private int getCount() {
        Connection connection = null;
        int count;
        try {
            connection = dbSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQLStatistic.GET_COUNT.QUERY);
            ResultSet set = statement.executeQuery();
            set.next();
            count = Integer.parseInt(set.getString("count"));
        } catch (SQLException exception) {
            count = 0;
        } finally {
            if (connection != null) {
                closeConnection(connection);
            }
        }
        return count;
    }

    private String getDateFirstEntryAdded() {
        Connection connection = null;
        String dateFirstEntryAdded;
        try {
            connection = dbSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQLStatistic.GET_DATE_FIRST_ENTRY_WAS_ADDED.QUERY);
            ResultSet set = statement.executeQuery();
            set.next();
            dateFirstEntryAdded = set.getString("date");
        } catch (SQLException exception) {
            dateFirstEntryAdded = null;
        } finally {
            if (connection != null) {
                closeConnection(connection);
            }
        }
        return dateFirstEntryAdded;
    }

    private String getDateLastEntryAdded() {
        Connection connection = null;
        String dateLastEntryAdded;
        try {
            connection = dbSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQLStatistic.GET_DATE_LAST_ENTRY_WAS_ADDED.QUERY);
            ResultSet set = statement.executeQuery();
            set.next();
            dateLastEntryAdded = set.getString("date");
        } catch (SQLException exception) {
            dateLastEntryAdded = null;
        } finally {
            if (connection != null) {
                closeConnection(connection);
            }
        }
        return dateLastEntryAdded;
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public enum SQLStatistic {
        GET_COUNT("SELECT COUNT(*) FROM cars"),
        GET_DATE_LAST_ENTRY_WAS_ADDED("SELECT date FROM cars ORDER BY date DESC LIMIT 1"),
        GET_DATE_FIRST_ENTRY_WAS_ADDED("SELECT date FROM cars ORDER BY date LIMIT 1");

        String QUERY;

        SQLStatistic(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
