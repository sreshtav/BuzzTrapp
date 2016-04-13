package buzztrapp.trapp.edit_trip;

import java.util.GregorianCalendar;

/**
 * Created by sreshtav on 4/13/16.
 */
public class TripItem {
    String id;
    String tripId;
    GregorianCalendar startTime;
    GregorianCalendar endTime;
    String interestPointId;

    TripItem (String id, String tripId, GregorianCalendar startTime, GregorianCalendar endTime, String interestPointId) {
        this.id = id;
        this.tripId = tripId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interestPointId = interestPointId;
    }
}
