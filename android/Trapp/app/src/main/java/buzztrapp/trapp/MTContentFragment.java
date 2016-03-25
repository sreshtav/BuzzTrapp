package buzztrapp.trapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Aaron on 3/24/2016.
 */
public class MTContentFragment extends Fragment{


    View noTrippImage;
    View noTrippText;
    RecyclerView rv;

    private List<Trip> trips;
    private List<Trip> fullTripsList;
    private ManageTripsRVAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mt_content_fragment_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        noTrippImage = getActivity().findViewById(R.id.noTripsImage);
        noTrippText = getActivity().findViewById(R.id.noTripsText);
        rv = (RecyclerView)getActivity().findViewById(R.id.rv);

    }

    public void addTrip(int noOfTrips)
    {
        if(noOfTrips == 0)
        {
            ((ViewGroup) noTrippImage.getParent()).addView(noTrippImage);
            ((ViewGroup) noTrippText.getParent()).addView(noTrippText);
        }
        if(noOfTrips == 1)
        {
            ((ViewGroup) noTrippImage.getParent()).removeView(noTrippImage);
            ((ViewGroup) noTrippText.getParent()).removeView(noTrippText);
        }

        if(noOfTrips > 0)
        {



            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(llm);


            initialiseData(noOfTrips);
            initialiseAdapter();

            adapter = new ManageTripsRVAdapter(trips);
            rv.setAdapter(adapter);
        }
    }

    private void initialiseData(int noOfTrips){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        trips = new ArrayList<>();
        fullTripsList = new ArrayList<>();
        fullTripsList.add(new Trip("New York", new GregorianCalendar(2015,12,03), new GregorianCalendar(2015,12,15), R.drawable.newyork));
        fullTripsList.add(new Trip("Singapore", new GregorianCalendar(2016, 01, 10), new GregorianCalendar(2016, 02, 03), R.drawable.singapore));
        fullTripsList.add(new Trip("Atlanta", new GregorianCalendar(2016,12,03), new GregorianCalendar(2016,12,15), R.drawable.atlanta));
        fullTripsList.add(new Trip("San Francisco", new GregorianCalendar(2016,8,04), new GregorianCalendar(2016,8,26), R.drawable.sanfrancisco));

        trips = fullTripsList.subList(0,noOfTrips-1);
    }

    private void initialiseAdapter(){
    }


}

/*

if(noOfTrips > 0)
        {
        noOfTrips++;
        View noTrippImage= findViewById(R.id.noTripsImage);
        ((ViewGroup) noTrippImage.getParent()).removeView(noTrippImage);
        View noTrippText = findViewById(R.id.noTripsText);
        ((ViewGroup) noTrippText.getParent()).removeView(noTrippText);


        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);


        initialiseData();
        initialiseAdapter();

        adapter = new ManageTripsRVAdapter(trips);
        rv.setAdapter(adapter);
        }*/
