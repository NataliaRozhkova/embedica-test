package cars.data.repository;

import cars.data.Response;
import cars.data.db.CarDBDataSource;
import cars.data.entity.Car;
import cars.data.entity.Statistic;

import java.util.List;

public class Repository {

    CarDBDataSource db = new CarDBDataSource();

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
        return db.getStatistic();
    }


}
