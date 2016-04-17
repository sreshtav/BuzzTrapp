package buzztrapp.trapp.edit_trip;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;

/**
 * Created by Aaron on 4/16/2016.
 */
public class EventView extends WeekViewEvent{

    private long id;
    private String tripItem_id;
    private String name;
    private String location;
    private Calendar startTime;
    private Calendar endTime;

    private WeekViewEvent weekViewEvent;

    public EventView(long id, String tripItem_id, String name, Calendar startTime, Calendar endTime){
        weekViewEvent = new WeekViewEvent(id,name,startTime,endTime);
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tripItem_id = tripItem_id;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public String getTripItem_id() {
        return tripItem_id;
    }

    public void setTripItem_id(String tripItem_id) {
        this.tripItem_id = tripItem_id;
    }

    public WeekViewEvent getWeekViewEvent() {
        return weekViewEvent;
    }

    public void setWeekViewEvent(WeekViewEvent weekViewEvent) {
        this.weekViewEvent = weekViewEvent;
    }
}
