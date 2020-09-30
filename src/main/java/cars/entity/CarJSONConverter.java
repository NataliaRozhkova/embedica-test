package cars.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class CarJSONConverter {

    public static String convertToJSON(final Car car) {
        try {
            return new ObjectMapper().writeValueAsString(car);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Car deserialize(final String json) {
        try {
            return new ObjectMapper().readValue(json, Car.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertToJSON (final List<Car> cars) {
        try {
            return new ObjectMapper().writeValueAsString(cars);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
