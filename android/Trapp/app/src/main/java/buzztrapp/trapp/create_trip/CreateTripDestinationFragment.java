package buzztrapp.trapp.create_trip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import buzztrapp.trapp.R;
import buzztrapp.trapp.create_trip.helper.ItemTouchHelperAdapter;
import buzztrapp.trapp.create_trip.helper.OnStartDragListener;
import buzztrapp.trapp.create_trip.helper.SimpleItemTouchHelperCallback;

public class CreateTripDestinationFragment extends android.support.v4.app.Fragment implements OnStartDragListener{

    public static final String ARG_OBJECT = "object";

    RecyclerView rv;
    AutoCompleteTextView dest_tv;
    ArrayAdapter<String> dest_tvAdapter;

    private CreateTripsRVAdapter adapter;

    private List<Destination> destinations;

    private ItemTouchHelper mItemTouchHelper;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_trip_destination, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        rv = (RecyclerView) getActivity().findViewById(R.id.ct_rv);

        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        dest_tv = (AutoCompleteTextView) getActivity().findViewById(R.id.dest_et);
        dest_tvAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, DESTINATIONS);
        dest_tv.setAdapter(dest_tvAdapter);

        destinations = new ArrayList<>();
//        destinations.add(new Destination("Hawaii", R.drawable.hawaii));

        dest_tv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                String location = selection.replaceAll("[^A-Za-z]+", "").toLowerCase();
                location = "@drawable/" + location;
                int image_resrc = getResources().getIdentifier(location, null, getActivity().getPackageName());

                destinations.add(new Destination(selection, image_resrc));
                if (destinations.size() == 0)
                    return;
                adapter.notifyDataSetChanged();

                ((CreateTripActivity) getActivity()).setDestinations(destinations);

                dest_tv.setText("");
            }
        });

        adapter = new CreateTripsRVAdapter(getContext(), destinations, this);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rv);


        rv.setAdapter(adapter);

    }


    private static final String[] DESTINATIONS = new String[]{
            "Hawaii", "Miami", "New York", "Singapore", "Atlanta", "San Francisco"
    };

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}
