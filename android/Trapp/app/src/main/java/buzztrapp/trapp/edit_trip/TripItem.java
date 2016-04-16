package buzztrapp.trapp.edit_trip;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Created by sreshtav on 4/13/16.
 */
public class TripItem implements Serializable{
    String id;
    String tripId;
    GregorianCalendar startTime;
    GregorianCalendar endTime;
    String city;
    String interest;
    int averageTime;
    String description;
    String address;
    String name;

    public TripItem (String id, String tripId, GregorianCalendar startTime, GregorianCalendar endTime, String city,
                     String interest, int averageTime, String description, String address, String name) {
        this.id = id;
        this.tripId = tripId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.city = city;
        this.interest = interest;
        this.averageTime = averageTime;
        this.description = description;
        this.address = address;
        this.name = name;
    }
}
