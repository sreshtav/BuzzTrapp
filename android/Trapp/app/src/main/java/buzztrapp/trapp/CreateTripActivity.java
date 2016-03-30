package buzztrapp.trapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class CreateTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_trip_activity);

        getSupportActionBar().setTitle("Manage Trips");


        TabHost createTripsTabs = (TabHost)findViewById(R.id.ct_tabhost);

        TabSpec destination_tab = createTripsTabs.newTabSpec("Destination");
        TabSpec duration_tab = createTripsTabs.newTabSpec("Duration");

        destination_tab.setIndicator("Tab1");
        destination_tab.setContent(new Intent(this,CreateTripDestinationActivity.class));

        duration_tab.setIndicator("Tab2");
        duration_tab.setContent(new Intent(this,CreateTripDurationActivity.class));

    }
}
