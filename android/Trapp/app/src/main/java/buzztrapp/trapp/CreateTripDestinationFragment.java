package buzztrapp.trapp;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class CreateTripDestinationFragment extends android.support.v4.app.Fragment {

    public static final String ARG_OBJECT = "object";

    RecyclerView rv;
    AutoCompleteTextView dest_tv;
    ArrayAdapter<String> dest_adapter;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_trip_destination, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rv = (RecyclerView) getActivity().findViewById(R.id.ct_rv);
        dest_tv = (AutoCompleteTextView) getActivity().findViewById(R.id.dest_et);

        dest_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, DESTINATIONS);

        dest_tv.setAdapter(dest_adapter);
    }

    private static final String[] DESTINATIONS = new String[]{
            "Hawaii", "Miami", "New York", "Singapore", "Atlanta", "San Francisco"
    };
}
