package buzztrapp.trapp.edit_trip;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.GregorianCalendar;

import buzztrapp.trapp.R;

/**
 * Created by Aaron on 4/12/2016.
 */
public class EditTripBottomFragment extends Fragment{


    private String tripid;
    private TextView testTV;

    GregorianCalendar date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.edit_trip_bottom_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        date = new GregorianCalendar();
        testTV = (TextView) getActivity().findViewById(R.id.textView6);
        tripid = ((EditTripActivity)getActivity()).getTripid();
//        date = ((EditTripActivity)getActivity()).getSelectedDate();
        testTV.setText("id = "+ tripid);
    }

    public void changeDate(GregorianCalendar date){
        this.date = date;
        testTV.setText("id = "+ tripid + ", SelectedDate = "+ this.date.getTime().toString());
    }

}
