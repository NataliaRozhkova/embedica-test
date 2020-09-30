package cars.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Car {
    @JsonProperty("id")
    public final long id;
    @JsonProperty("number")
    public final String number;
    @JsonProperty("brand")
    public final String brand;
    @JsonProperty("color")
    public final String color;
    @JsonProperty("year_of_issue")
    public final int yearOfIssue;


    @JsonCreator
    public Car(@JsonProperty("id") long id,
               @JsonProperty("number") String number,
               @JsonProperty("brand") String brand,
               @JsonProperty("color") String color,
               @JsonProperty("year_of_issue") int yearOfIssue) {
        this.id = id;
        this.number = number;
        this.brand = brand;
        this.color = color;
        this.yearOfIssue = yearOfIssue;

    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Car)) {
            return false;
        }
        if (obj != null
                && ((Car) obj).yearOfIssue == this.yearOfIssue
                && this.color.equals(((Car) obj).color)
                && this.brand.equals(((Car) obj).brand)
                && this.number.equals(((Car) obj).number)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime * (int) id + number.hashCode() + brand.hashCode() + color.hashCode() + prime * yearOfIssue;
    }


}
