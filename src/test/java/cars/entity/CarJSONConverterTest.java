package cars.entity;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class CarJSONConverterTest extends TestCase {


    public void testConvertToJSON() {
        Car car1 = new Car(1, "vv111q", "brand", "black", 1999);
        Car car2 = new Car(2, "nn222nn", "brand", "blue", 2000);

        StringBuilder json = new StringBuilder();
        json.append("{\"id\":")
                .append(car1.id)
                .append(",\"number\":\"")
                .append(car1.number)
                .append("\",\"color\":\"")
                .append(car1.color)
                .append("\",\"year_of_issue\":")
                .append(car1.yearOfIssue)
                .append("}");
        String convertJson = CarJSONConverter.convertToJSON(car1);
        assertEquals(json.toString(), convertJson);
        assertNotSame(json.toString(), car2);
    }

    public void testDeserialize() {
        Car car1 = new Car(1, "dd333dd", "brand", "blue", 2010);
        Car car2 = new Car(2, "dd333dd", "brand", "blue", 2010);

        String json = CarJSONConverter.convertToJSON(car1);

        assertEquals(car1, CarJSONConverter.deserialize(json));
        assertNotSame(car2, CarJSONConverter.deserialize(json));


    }

    public void testTestConvertToJSON() {
        Car car1 = new Car(1, "dd333dd", "brand", "blue", 2010);
        Car car2 = new Car(2, "dd333dd", "brand", "blue", 2010);

        List<Car> list = new ArrayList<>();
        list.add(car1);
        list.add(car2);

        StringBuilder json = new StringBuilder();
        json.append("[")
                .append(CarJSONConverter.convertToJSON(car1))
                .append(",")
                .append(CarJSONConverter.convertToJSON(car2))
                .append("]");

        assertEquals(json.toString(), CarJSONConverter.convertToJSON(list));
    }
}