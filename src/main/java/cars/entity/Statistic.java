package cars.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Statistic {
    @JsonProperty("number_of_records")
    public final int numberOfRecords;
    @JsonProperty("date_first_entry_was_added")
    public final String dateFirstEntryWasAdded;
    @JsonProperty("date_last_entry_was_added")
    public final String dateLastEntryWasAdded;


    @JsonCreator
    public Statistic(
            @JsonProperty("number_of_records") int numberOfRecords,
            @JsonProperty("date_first_entry_was_added") String dateFirstEntryWasAdded,
            @JsonProperty("date_last_entry_was_added") String dateLastEntryWasAdded) {
        this.numberOfRecords = numberOfRecords;
        this.dateFirstEntryWasAdded = dateFirstEntryWasAdded;
        this.dateLastEntryWasAdded = dateLastEntryWasAdded;

    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Statistic)) {
            return false;
        }
        if (obj != null
                && ((Statistic) obj).numberOfRecords == this.numberOfRecords
                && this.dateFirstEntryWasAdded.equals(((Statistic) obj).dateFirstEntryWasAdded)
                && this.dateLastEntryWasAdded.equals(((Statistic) obj).dateLastEntryWasAdded)
        ) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime * (int) numberOfRecords + dateLastEntryWasAdded.hashCode() + dateFirstEntryWasAdded.hashCode();
    }


}
