package buzztrapp.trapp.edit_trip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import buzztrapp.trapp.R;
import buzztrapp.trapp.manage_trips.ManageTripsActivity;

public class EditEventActivity extends AppCompatActivity implements View.OnClickListener{

    private GregorianCalendar start = new GregorianCalendar();
    private GregorianCalendar end = new GregorianCalendar();

    private String name;
    private String location;
    private android.support.v7.widget.Toolbar toolbar;

    private SimpleDateFormat sdf;

    private boolean new_event;

    private String type;

//    Form variables
    EditText title_et;

    LinearLayout date_ll;
    TextView date_tv;
    LinearLayout time_ll;
    TextView startTime_tv;
    TextView endTime_tv;

    LinearLayout loc_ll;
    LinearLayout alarm_ll;
    LinearLayout transport_ll;


    DatePickerDialog dateDialog;
    TimePickerDialog startTimeDialog;
    TimePickerDialog endTimeDialog;

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




        title_et = (EditText) findViewById(R.id.ee_title_et);
        loc_ll = (LinearLayout) findViewById(R.id.ee_location);
        alarm_ll = (LinearLayout) findViewById(R.id.ee_alarm);
        date_ll = (LinearLayout) findViewById(R.id.ee_date);
        date_tv = (TextView) findViewById(R.id.ee_date_tv);
        time_ll = (LinearLayout) findViewById(R.id.ee_time);
        startTime_tv = (TextView) findViewById(R.id.ee_start_time_tv);
        endTime_tv = (TextView) findViewById(R.id.ee_end_time_tv);

        transport_ll = (LinearLayout) findViewById(R.id.ee_transportation);


        date_ll.setOnClickListener(this);
        sdf = new SimpleDateFormat("MMM dd", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                GregorianCalendar newDate = new GregorianCalendar();
                newDate.set(year, monthOfYear, dayOfMonth);
                final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                date_tv.setText(days[newDate.get(Calendar.DAY_OF_WEEK)-1]+ ", " + sdf.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        startTime_tv.setOnClickListener(this);

        startTimeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String ampm = "am";
                if(selectedHour>12) {
                    selectedHour -= 12;
                    ampm = "pm";
                }
                startTime_tv.setText(selectedHour + ":" + selectedMinute + ampm);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);

        startTimeDialog.setTitle("Select Start Time");

        endTime_tv.setOnClickListener(this);

        endTimeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String ampm = "am";
                if(selectedHour>12) {
                    selectedHour -= 12;
                    ampm = "pm";
                }
                endTime_tv.setText(selectedHour + ":" + selectedMinute + ampm);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);

        endTimeDialog.setTitle("Select End Time");

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


    @Override
    public void onClick(View v) {
        if(v == date_ll) {
            Toast.makeText(EditEventActivity.this, "Date Selected " ,
                    Toast.LENGTH_SHORT).show();
            dateDialog.show();
        }
        else if(v == startTime_tv){

            Toast.makeText(EditEventActivity.this, "Date Selected " ,
                    Toast.LENGTH_SHORT).show();
            startTimeDialog.show();
        }
        else if(v == endTime_tv){

            Toast.makeText(EditEventActivity.this, "Date Selected " ,
                    Toast.LENGTH_SHORT).show();
            endTimeDialog.show();
        }
    }
}
