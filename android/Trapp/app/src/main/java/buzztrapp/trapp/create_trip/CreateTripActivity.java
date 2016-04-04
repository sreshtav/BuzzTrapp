package buzztrapp.trapp.create_trip;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import buzztrapp.trapp.R;
import buzztrapp.trapp.manage_trips.ManageTripsActivity;

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

        getSupportActionBar().setTitle("Manage Trips");

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
            Intent intent = new Intent(this, ManageTripsActivity.class);
            this.startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);

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

