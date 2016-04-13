package buzztrapp.trapp.edit_trip;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import buzztrapp.trapp.Communicator;
import buzztrapp.trapp.R;
import buzztrapp.trapp.create_trip.CreateTripActivity;
import buzztrapp.trapp.manage_trips.ManageTripsActivity;

public class EditTripActivity extends AppCompatActivity{


    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerListener;
    private android.support.v7.widget.Toolbar toolbar;
    private Menu menu;

    EditTripTopFragment topFragment;
    FragmentManager manager;
    FragmentTransaction transaction;

    GregorianCalendar startDate;
    GregorianCalendar endDate;
    GregorianCalendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_trip_activity);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.et_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit Itinerary");

        drawerLayout = (DrawerLayout) findViewById(R.id.et_layout);
        drawerListView = (ListView) findViewById(R.id.et_drawer_listview);


        //@Sreshta: Insert getting of dates here
        //startDate = <get start date from DB>
        //endDate = <get end date from DB>
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startDate = new GregorianCalendar();
        endDate = new GregorianCalendar();
        selectedDate = new GregorianCalendar();
        String startDateString = "2016-04-15";
        String endDateString = "2016-04-21";
        try{
            startDate.setTime(sdf.parse(startDateString));
            endDate.setTime(sdf.parse(endDateString));
        }catch(ParseException p){

        }

        //remove example dates above

        topFragment = new EditTripTopFragment();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.et_body_layout, topFragment, "EditTripTopFragment");
        transaction.commit();


        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(EditTripActivity.this, "Drawer Closed",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(EditTripActivity.this, "Drawer Closed",
                        Toast.LENGTH_SHORT).show();
            }


        };
        drawerLayout.setDrawerListener(drawerListener);
    }


    public GregorianCalendar getStartDate(){
        return startDate;
    }

    public GregorianCalendar getEndDate(){
        return endDate;
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
//        menu.findItem(R.id.action_done).setEnabled(false);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.edit_trip_menu, menu);

        return true;

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

    //noinspection SimplifiableIfStatement

       /* if (id == R.id.action_done) {
            Intent intent = new Intent(this, ManageTripsActivity.class);
            this.startActivity(intent);
            return true;

        }*/
        return super.onOptionsItemSelected(item);

    }

    //Overridden from the Communicator Implementation
    //Communicator is for inter-fragment communication
    public void setSelectedDate(Date date) {
        selectedDate.setTime(date);
    }

    //just for debugging, remove if not required
    public void logToast(){
        /*Toast.makeText(EditTripActivity.this, "Date Selected "+selectedDate.getTime().toString(),
                Toast.LENGTH_SHORT).show();*/
    }
}
