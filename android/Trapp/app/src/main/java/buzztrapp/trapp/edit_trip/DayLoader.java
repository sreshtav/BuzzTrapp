package buzztrapp.trapp.edit_trip;

import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Aaron on 4/13/2016.
 */
public class DayLoader implements WeekViewLoader {

    private DayChangeListener mDayChangeListener;
    private int lastPeriodIndex;
    private Calendar lastCalendar;

    @Override
    public double toWeekViewPeriodIndex(Calendar instance) {
        double periodIndex = instance.getTimeInMillis() / (1000.0 * 60 * 60 * 24);
        lastPeriodIndex = (int) periodIndex;
        lastCalendar = instance;
        return periodIndex;
    }

    @Override
    public List<? extends WeekViewEvent> onLoad(int periodIndex) {
        Calendar day = null;
        int diffDays = lastPeriodIndex - periodIndex;
        if (diffDays == 0) {
            day = lastCalendar;
        } else {
            day = (Calendar) lastCalendar.clone();
            day.add(Calendar.DAY_OF_MONTH, -1 * diffDays);
        }

        return mDayChangeListener.onDayChange(day.get(Calendar.YEAR), day.get(Calendar.MONTH),
                day.get(Calendar.DAY_OF_MONTH));
    }

    public DayChangeListener getDayChangeListener() {
        return mDayChangeListener;
    }

    public void setDayChangeListener(DayChangeListener mDayChangeListener) {
        this.mDayChangeListener = mDayChangeListener;
    }

    public interface DayChangeListener {
        List<? extends WeekViewEvent> onDayChange(int year, int month, int dayOfMonth);
    }
}
