package buzztrapp.trapp.edit_trip;

import android.app.Fragment;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import buzztrapp.trapp.R;

/**
 * Created by Aaron on 4/12/2016.
 */
public class EditTripBottomFragment extends Fragment{


    private String tripid;
    private TextView testTV;

    private List<TripItem> tripItems;

    GregorianCalendar date;

    WeekView mWeekView;
    WeekViewEvent event;

    WeekView.EventClickListener mEventClickListener;
    MonthLoader.MonthChangeListener mMonthChangeListener;
    WeekView.EventLongPressListener mEventLongPressListener;

    private WeekViewLoader weekViewLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.edit_trip_bottom_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        date = new GregorianCalendar();
        date = ((EditTripActivity)getActivity()).getSelectedDate();
//        testTV = (TextView) getActivity().findViewById(R.id.textView6);
        tripid = ((EditTripActivity)getActivity()).getTripid();
//        testTV.setText("id = "+ tripid);

        tripItems = ((EditTripActivity)getActivity()).getTripItems();


        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) getActivity().findViewById(R.id.weekView);
// Set an action when any event is clicked.

        mEventClickListener = new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {

            }
        };

        mWeekView.setOnEventClickListener(mEventClickListener);

        weekViewLoader = new WeekViewLoader() {
            @Override
            public double toWeekViewPeriodIndex(Calendar instance) {
                return 0;
            }

            @Override
            public List<? extends WeekViewEvent> onLoad(int periodIndex) {
                List<WeekViewEvent> events = new ArrayList<>();

                for (int i = 0; i<tripItems.size(); i++){
                    event = new WeekViewEvent(i, "id "+tripItems.get(i).id+" from "+tripItems.get(i).tripId, tripItems.get(i).startTime, tripItems.get(i).endTime);
                    events.add(event);
                }
                return events;
            }
        };

        mWeekView.setWeekViewLoader(weekViewLoader);

        weekViewLoader.onLoad((int)weekViewLoader.toWeekViewPeriodIndex(date));

        mWeekView.goToDate(date);

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
        MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                List<WeekViewEvent> events = new ArrayList<>();

                for (int i = 0; i<tripItems.size(); i++){
                    event = new WeekViewEvent(i, "id "+tripItems.get(i).id+" from "+tripItems.get(i).tripId, tripItems.get(i).startTime, tripItems.get(i).endTime);
                    events.add(event);
                }
                return events;
            }
        };
        mWeekView.setMonthChangeListener(mMonthChangeListener);

// Set long press listener for events.
        mWeekView.setEventLongPressListener(mEventLongPressListener);


    }

    public void changeDate(GregorianCalendar date){
        this.date = date;
//        testTV.setText("id = "+ tripid + ", SelectedDate = "+ this.date.getTime().toString());
        mWeekView.goToDate(date);
    }


}
