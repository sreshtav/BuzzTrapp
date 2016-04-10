package buzztrapp.trapp.manage_trips;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import buzztrapp.trapp.Communicator;
import buzztrapp.trapp.R;
import buzztrapp.trapp.create_trip.CreateTripActivity;

public class ManageTripsActivity extends AppCompatActivity implements Communicator {

    private RecyclerView rv;
    private List<Trip> trips;
    private List<Trip> fullTripsList;
    private ManageTripsRVAdapter adapter;
    private int noOfTrips = 0;

    ManageTripsFragment contentFragment;
    FragmentManager manager;
    FragmentTransaction transaction;

    private DrawerLayout drawerLayout;
    private ListView drawerlistView;

    private ActionBarDrawerToggle drawerListener;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_trips_activity);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.mt_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Manage Trips");
        Log.d("Aaron", "Main: onCreate and about to add contentFragment");



        contentFragment = new ManageTripsFragment();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.mt_body_layout, contentFragment, "ManageTripsFragment");
        transaction.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                noOfTrips++;
                noOfTrips = noOfTrips%4;
                Snackbar.make(view, "no of Trips = "+noOfTrips, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                contentFragment.addTrip(noOfTrips);*/

                Intent intent = new Intent(view.getContext(), CreateTripActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.mt_layout);
        drawerlistView = (ListView) findViewById(R.id.drawer_listview);


        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(ManageTripsActivity.this, "Drawer Closed",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(ManageTripsActivity.this, "Drawer Closed",
                        Toast.LENGTH_SHORT).show();
            }


        };
        drawerLayout.setDrawerListener(drawerListener);


    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }



    @Override
    public void respond(String data) {
    }
}