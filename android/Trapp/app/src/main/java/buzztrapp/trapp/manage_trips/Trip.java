package buzztrapp.trapp.manage_trips;

import java.util.GregorianCalendar;

/**
 * Created by Aaron on 3/6/2016.
 */
class Trip{
    String location;
    GregorianCalendar startDate;
    GregorianCalendar endDate;
    int imageId;

    Trip(String location, GregorianCalendar startDate, GregorianCalendar endDate, int imageId){
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageId = imageId;
    }
}
