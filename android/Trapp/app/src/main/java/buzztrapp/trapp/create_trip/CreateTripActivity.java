package buzztrapp.trapp.create_trip;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import buzztrapp.trapp.R;
import buzztrapp.trapp.manage_trips.ManageTripsActivity;
import cz.msebera.android.httpclient.Header;

public class CreateTripActivity extends AppCompatActivity implements ActionBar.TabListener{



    private DrawerLayout drawerLayout;
    private ListView drawerlistView;
    private ActionBarDrawerToggle drawerListener;
    private android.support.v7.widget.Toolbar toolbar;

    private Menu menu;

    private List<Date> dates;
    private List<Destination> destinations;


    CreateTripPagerAdapter ctPageAdapter;
    ViewPager mViewPager;

    SlidingTabLayout mSlidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_trip_activity);


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.ct_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Create a Trip");

        ctPageAdapter = new CreateTripPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.ct_viewpager);
        mViewPager.setAdapter(ctPageAdapter);


        destinations = new ArrayList<Destination>();
        dates = new ArrayList<Date>();


        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.ct_slidingTabLayout);
        mSlidingTabLayout.setDistributeEvenly(true);
//        mSlidingTabLayout.setSelectedIndicatorColors(R.color.bittersweet);
        mSlidingTabLayout.setViewPager(mViewPager);
//        mSlidingTabLayout.bringToFront();
//        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.trappBlue));

        drawerLayout = (DrawerLayout) findViewById(R.id.ct_layout);
        drawerlistView = (ListView) findViewById(R.id.ct_drawer_listview);


        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(CreateTripActivity.this, "Drawer Closed",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(CreateTripActivity.this, "Drawer Closed",
                        Toast.LENGTH_SHORT).show();
            }


        };
        drawerLayout.setDrawerListener(drawerListener);

    }

    public void setDates(List<Date> dates){
        this.dates = dates;
        ct_ready();
    }

    public void setDestinations(List<Destination>destinations){
        this.destinations = destinations;
        ct_ready();
    }

    public void ct_ready(){
        if(!dates.isEmpty() && !destinations.isEmpty())
        {
            menu.findItem(R.id.action_done).setEnabled(true);
        }
        else{
            menu.findItem(R.id.action_done).setEnabled(false);
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerListener.syncState();
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_done).setEnabled(false);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.create_trip_menu, menu);

        return true;

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will

        // automatically handle clicks on the Home/Up button, so long

        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

//noinspection SimplifiableIfStatement

        if (id == R.id.action_done) {
            addTripToDatabase();
            Intent intent = new Intent(this, ManageTripsActivity.class);
            this.startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    private void addTripToDatabase () {
        Log.d("CreateTrip", "Inside getTrips");
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", preferences.getString("token", ""));
        RequestParams params = new RequestParams();
        Log.d("CreateTrip", "Location - " + destinations.get(0).location);
        Log.d("CreateTrip", "Start date - " + dates.get(0));
        Log.d("CreateTrip", "End date - " + dates.get(1));
        params.put("location", destinations.get(0).location);
        params.put("startDate", dates.get(0));
        params.put("endDate", dates.get(dates.size()-1));
        client.post("http://173.236.255.240/api/addTrip", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("CreateTrip", "Inside success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d("CreateTrip", "Inside failure");
            }
        });
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());

    }
}

