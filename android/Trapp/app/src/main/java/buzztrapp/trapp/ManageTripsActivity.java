package buzztrapp.trapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

public class ManageTripsActivity extends AppCompatActivity implements Communicator{

    private RecyclerView rv;
    private List<Trip> trips;
    private List<Trip> fullTripsList;
    private ManageTripsRVAdapter adapter;
    private int noOfTrips = 0;

    MTContentFragment contentFragment;
    FragmentManager manager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trips);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        if (title != null) {
            title.setText("Manage Trips");
        }

        contentFragment = new MTContentFragment();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.mt_content_layout, contentFragment, "MTContentFragment");
        transaction.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                noOfTrips++;
                noOfTrips = noOfTrips%4;
                Snackbar.make(view, "no of Trips = "+noOfTrips, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                contentFragment.addTrip(noOfTrips);
            }
        });



    }




    @Override
    public void respond(String data) {
    }
}
