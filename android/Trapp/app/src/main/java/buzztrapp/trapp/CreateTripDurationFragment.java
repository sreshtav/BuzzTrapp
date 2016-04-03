package buzztrapp.trapp;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

public class CreateTripDurationFragment extends android.support.v4.app.Fragment {

    public static final String ARG_OBJECT = "object";
    CalendarPickerView calendar;

    CalendarPickerView.OnDateSelectedListener dateSelectedListener;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.create_trip_duration, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        calendar = (CalendarPickerView) getActivity().findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE);

        dateSelectedListener = new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                ((CreateTripActivity)getActivity()).setDates(calendar.getSelectedDates());
            }

            @Override
            public void onDateUnselected(Date date) {
                ((CreateTripActivity)getActivity()).setDates(calendar.getSelectedDates());
            }
        };

        calendar.setOnDateSelectedListener(dateSelectedListener);

    }
}
