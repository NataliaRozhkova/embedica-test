package cars.data.repository;

import cars.data.Response;
import cars.data.db.CarDBSource;
import cars.entity.Car;
import cars.entity.Statistic;

import java.util.List;

public class Repository {

    CarDBSource db = new CarDBSource("jdbc:postgresql://localhost:5432/", "carsuser", "123", "cars", "cars");

    public Response<String> put(final Car car) {
        return db.create(car);
    }

    public Response<String> delete(final long carId) {
        return db.delete(carId);
    }

    public Response<List<Car>> getAll(final String sortParam, final int limit, final int offset) {
        return db.getAll(sortParam, limit, offset);
    }

    public Response<List<Car>> getAll() {
        return db.getAll();
    }

    public Response<Statistic> getStatistic() {
        return  db.getStatistic();
    }


}
