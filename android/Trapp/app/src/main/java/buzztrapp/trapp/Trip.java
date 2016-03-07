package buzztrapp.trapp;

import java.util.GregorianCalendar;

/**
 * Created by Aaron on 3/6/2016.
 */
class Trip{
    String name;
    GregorianCalendar startDate;
    GregorianCalendar endDate;
    int imageId;

    Trip(String name, GregorianCalendar startDate, GregorianCalendar endDate, int imageId){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageId = imageId;
    }
}
