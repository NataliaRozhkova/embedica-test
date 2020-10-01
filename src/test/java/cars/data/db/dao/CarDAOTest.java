package cars.data.db.dao;

import cars.data.Response;
import cars.data.db.CarDBSource;
import cars.entity.Car;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.sql.SQLException;
import java.util.List;

class CarDAOTest {

    private static CarDBSource db;


    @BeforeAll
    public static void createDB() {

        db = new CarDBSource("jdbc:postgresql://localhost:5432/", "carsuser", "123", "example", "cars");

    }

    @AfterAll
    public static void deleteDb() throws SQLException {
        db.getDataSource().getConnection().prepareStatement("DROP TABLE cars").executeUpdate();
//        db.getDataSource().getConnection().prepareStatement("DROP database example").executeUpdate();
    }

    @Test
    void create() {

        Car car = new Car(1, "n111nn", "brand", "f", 2000);
        Response<String> response = db.create(car);
        assertEquals(Response.State.SUCCESS, response.state);
        response = db.create(car);
        assertEquals(Response.State.ERROR, response.state);
    }


    @Test
    void readAll() {
        Car car = new Car(2, "n133nn", "brand", "f", 2003);
        db.create(car);
        List<Car> list = db.getAll().body;
        assertNotEquals(0, list.size());
    }


    @Test
    void delete() {
        Car car = new Car(3, "n1141nn", "brand", "f", 2000);
        db.create(car);
        List<Car> list = db.getAll().body;
        car = list.get(0);
        Response<String> response = db.delete(car.id);
        assertEquals(Response.State.SUCCESS, response.state);

        assertEquals(list.size() - 1, db.getAll().body.size());
    }
}