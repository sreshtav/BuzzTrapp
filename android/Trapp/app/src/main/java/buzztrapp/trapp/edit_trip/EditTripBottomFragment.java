package buzztrapp.trapp.edit_trip;

import android.app.Fragment;
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

    GregorianCalendar date;

    WeekView mWeekView;

    WeekView.EventClickListener mEventClickListener;
    MonthLoader.MonthChangeListener mMonthChangeListener;
    WeekView.EventLongPressListener mEventLongPressListener;

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


        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) getActivity().findViewById(R.id.weekView);
// Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(mEventClickListener);

        mWeekView.goToDate(date);

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
        MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                List<WeekViewEvent> events = new ArrayList<>();
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
