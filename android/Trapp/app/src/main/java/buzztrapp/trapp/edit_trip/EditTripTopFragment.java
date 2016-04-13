package buzztrapp.trapp.edit_trip;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

import buzztrapp.trapp.R;
import buzztrapp.trapp.create_trip.CreateTripActivity;

public class EditTripTopFragment extends Fragment {

    CalendarPickerView calendar;

    CalendarPickerView.OnDateSelectedListener dateSelectedListener;

    Date startDate;
    Date endDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("Aaron", "Fragment: onCreateView()");
        return inflater.inflate(R.layout.edit_trip_top_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        calendar = (CalendarPickerView) getActivity().findViewById(R.id.calendar_view);

        startDate = ((EditTripActivity)getActivity()).getStartDate().getTime();
        endDate = ((EditTripActivity)getActivity()).getEndDate().getTime();

        calendar.init(startDate, endDate).inMode(CalendarPickerView.SelectionMode.SINGLE);

        dateSelectedListener = new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                ((EditTripActivity)getActivity()).setSelectedDate(date);
                ((EditTripActivity)getActivity()).logToast();

            }

            @Override
            public void onDateUnselected(Date date) {
//                ((EditTripActivity)getActivity()).setDates(calendar.getSelectedDates());
            }
        };

        calendar.setOnDateSelectedListener(dateSelectedListener);



    }

}
