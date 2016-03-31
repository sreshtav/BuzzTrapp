package buzztrapp.trapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class CreateTripActivity extends AppCompatActivity {



    private DrawerLayout drawerLayout;
    private ListView drawerlistView;
    private ActionBarDrawerToggle drawerListener;
    private android.support.v7.widget.Toolbar toolbar;

    CreateTripFragment contentFragment;
    FragmentManager manager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_trip_activity);


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.ct_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Manage Trips");


        contentFragment = new CreateTripFragment();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.ct_body_layout, contentFragment, "ManageTripsFragment");
        transaction.commit();

    }
}
