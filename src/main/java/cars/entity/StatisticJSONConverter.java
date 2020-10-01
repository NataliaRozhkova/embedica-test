package cars.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class StatisticJSONConverter {


    public static String convertToJSON(final Statistic statistic) {
        try {
            return new ObjectMapper().writeValueAsString(statistic);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
