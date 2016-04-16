package buzztrapp.trapp.edit_trip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import buzztrapp.trapp.R;
import buzztrapp.trapp.manage_trips.ManageTripsActivity;

public class EditEventActivity extends AppCompatActivity {

    private GregorianCalendar start = new GregorianCalendar();
    private GregorianCalendar end = new GregorianCalendar();

    private String name;
    private String location;
    private android.support.v7.widget.Toolbar toolbar;

    private boolean new_event;

    private String type;


    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event_activity);


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.ee_toolbar);
        setSupportActionBar(toolbar);


        Intent inIntent = getIntent();
        Bundle inBundle = inIntent.getExtras();

        name = inBundle.getString("name");
        location = inBundle.getString("location");
        GregorianCalendar startDate = (GregorianCalendar) inBundle.getSerializable("startTime");
        GregorianCalendar endDate = (GregorianCalendar) inBundle.getSerializable("endTime");

        if (name.equals("") && location.equals(""))
            new_event = true;
        else
            new_event = false;

        if(new_event)
            getSupportActionBar().setTitle("Add an Event");
        else
            getSupportActionBar().setTitle("Edit an Event");



/*        TextView tv = (TextView)findViewById(R.id.ee_textView);

        if (name.equals("") && location.equals("")){
            tv.setText("No Event found! Create one now! Start time = "+startDate.getTime().toString() +", end time = "+endDate.getTime().toString());
        }
        else{
            tv.setText("Event details: name = "+name + ", location = "+location + ", start time = "+startDate.getTime().toString() +", end time = "+endDate.getTime().toString());
        }*/
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if(new_event)
        {
            menu.findItem(R.id.action_save).setVisible(true);
            menu.findItem(R.id.action_delete).setVisible(false);
        }
        else
        {
            menu.findItem(R.id.action_save).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.edit_event_menu, menu);

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


}
