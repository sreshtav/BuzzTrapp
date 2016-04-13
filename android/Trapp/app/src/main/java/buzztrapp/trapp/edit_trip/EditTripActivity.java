package buzztrapp.trapp.edit_trip;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.app.FragmentManager;
import android.content.SharedPreferences;
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
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import buzztrapp.trapp.R;
import cz.msebera.android.httpclient.Header;

public class EditTripActivity extends AppCompatActivity{


    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerListener;
    private android.support.v7.widget.Toolbar toolbar;
    private Menu menu;

    EditTripTopFragment topFragment;
    EditTripBottomFragment bottomFragment;
    FragmentManager manager;
    FragmentTransaction transaction;

    GregorianCalendar startDate = new GregorianCalendar();
    GregorianCalendar endDate = new GregorianCalendar();
    GregorianCalendar selectedDate = new GregorianCalendar();

    private String tripid;

    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_trip_activity);


        //Best to change this part to retrieving details from db through the use of TRIPID
        //here im just going to get location and start date end date from text (wrong but temporary way)

//        String desc = getIntent().getStringExtra("desc");
//
//        //       trips.get(i).location + " | " + startDateString + " - " + endDateString
//
//        int i1 = desc.indexOf("|");
//        location = desc.substring(0,i1-1);
//
//        int i2 = desc.indexOf("-");
//        String startDateString = desc.substring(i1 + 2, i2 - 1);
//        String endDateString = desc.substring(i2 + 2);
//
//        int i3 = startDateString.indexOf(" ");
//        String startDateMonthString = startDateString.substring(0, i3);
//        String startDateDayString= startDateString.substring(i3 + 1);
//
//        if (startDateDayString.length() == 1){
//            startDateDayString = " 0".concat(startDateDayString);
//        }
//
//        int i4 = startDateString.indexOf(" ");
//        String endDateMonthString = endDateString.substring(0, i4);
//        String endDateDayString = endDateString.substring(i4 + 1);
//
//        if (endDateDayString.length() == 1){
//            endDateDayString = " 0".concat(endDateDayString);
//        }
//        Date startDateDate = new Date();
//        Date endDateDate = new Date();
//        try{
//            startDateDate = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH).parse(startDateMonthString.substring(0,3) +startDateDayString + " 2016");
//            endDateDate = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH).parse(endDateMonthString.substring(0,3)+endDateDayString + " 2016");
//        }catch(Exception e){
//
//        }
//
//        startDate.setTime(startDateDate);
//        endDate.setTime(endDateDate);

        //change the above

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.et_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit Itinerary");

        drawerLayout = (DrawerLayout) findViewById(R.id.et_layout);
        drawerListView = (ListView) findViewById(R.id.et_drawer_listview);


        //@Sreshta: Insert getting of dates here
        //startDate = <get start date from DB>
        //endDate = <get end date from DB>
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        Date sDate = (Date) bundle.getSerializable("startDate");
        Date eDate = (Date) bundle.getSerializable("endDate");

        long t = sDate.getTime();

        startDate = new GregorianCalendar();
        endDate = new GregorianCalendar();
        startDate.setTime(new Date(sDate.getTime()));
        endDate.setTime(new Date(eDate.getTime()));

        tripid = bundle.getString("id");

        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = new GregorianCalendar();
        String startDateString = "2016-04-15";
        String endDateString = "2016-04-21";
        try{
            startDate.setTime(sdf.parse(startDateString));
            endDate.setTime(sdf.parse(endDateString));
        }catch(ParseException p){

        }*/

        //remove example dates above

        topFragment = new EditTripTopFragment();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.et_body_layout, topFragment, "EditTripTopFragment");
        transaction.commit();

        bottomFragment = new EditTripBottomFragment();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.et_body_layout, bottomFragment, "EditTripBottomFragment");
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

    public void setDefaultDate(Date date){
        selectedDate.setTime(date);
    }
    //Overridden from the Communicator Implementation
    //Communicator is for inter-fragment communication
    public void setSelectedDate(Date date) {
        selectedDate.setTime(date);
        bottomFragment.changeDate(selectedDate);

    }

    //just for debugging, remove if not required
    public void logToast(){
        /*Toast.makeText(EditTripActivity.this, "Date Selected "+selectedDate.getTime().toString(),
                Toast.LENGTH_SHORT).show();*/
    }

    public String getTripid(){
        return tripid;
    }
    public GregorianCalendar getSelectedDate(){
        return selectedDate;
    }

    private ArrayList<TripItem> getTripItems () {
        final ArrayList<TripItem> tripItems = new ArrayList<TripItem>();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", preferences.getString("token", ""));
        RequestParams params = new RequestParams();
        client.get("http://173.236.255.240/api/myTripItems?tripId="+tripid, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String json = new String(response);
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    Log.d("EditTrip", "Got back " + jsonArray.length() + " tripItems");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        GregorianCalendar startDate = new GregorianCalendar();
                        startDate.setTime(sdf.parse(jsonObject.getString("startTime")));
                        GregorianCalendar endDate = new GregorianCalendar();
                        endDate.setTime(sdf.parse(jsonObject.getString("endTime")));

                        TripItem tripItem = new TripItem(jsonObject.getString("_id"), jsonObject.getString("tripId"), startDate, endDate, jsonObject.getString("interestPointId"));
                        tripItems.add(tripItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d("EditTrip", "Inside failure");
            }
        });
        return tripItems;
    }
}
