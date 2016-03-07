package buzztrapp.trapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class ManageTripsActivity extends AppCompatActivity {

    private RecyclerView rv;
    private List<Trip> trips;
    private List<Trip> fullTripsList;
    private ManageTripsRVAdapter adapter;
    private int noOfTrips = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trips);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        if (title != null) {
            title.setText("Manage Trips");
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
        }

    }


    private void initialiseData(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        trips = new ArrayList<>();
        fullTripsList = new ArrayList<>();
        fullTripsList.add(new Trip("New York", new GregorianCalendar(2015,12,03), new GregorianCalendar(2015,12,15), R.drawable.newyork));
        fullTripsList.add(new Trip("Singapore", new GregorianCalendar(2016,01,10), new GregorianCalendar(2016,02,03), R.drawable.singapore));
        fullTripsList.add(new Trip("Atlanta", new GregorianCalendar(2016,12,03), new GregorianCalendar(2016,12,15), R.drawable.atlanta));
        fullTripsList.add(new Trip("San Francisco", new GregorianCalendar(2016,8,04), new GregorianCalendar(2016,8,26), R.drawable.sanfrancisco));

        trips = fullTripsList.subList(0,noOfTrips-1);
    }

    private void initialiseAdapter(){
    }
}
