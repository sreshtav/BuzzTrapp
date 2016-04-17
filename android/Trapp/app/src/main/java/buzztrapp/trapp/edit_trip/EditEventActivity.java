package buzztrapp.trapp.edit_trip;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import buzztrapp.trapp.R;
import buzztrapp.trapp.manage_trips.ManageTripsActivity;
@TargetApi(23)
public class EditEventActivity extends AppCompatActivity implements View.OnClickListener{

    private GregorianCalendar start = new GregorianCalendar();
    private GregorianCalendar end = new GregorianCalendar();

    private String name;
    private String location;
    private android.support.v7.widget.Toolbar toolbar;

    private SimpleDateFormat sdf;

    private boolean new_event;

    private String type;

    private long id;

//    Form variables
    TextView title_tv;

    LinearLayout date_ll;
    TextView date_tv;
    LinearLayout time_ll;
    TextView startTime_tv;
    TextView endTime_tv;
    ImageView type_iv;

    TextView indays_tv;

    LinearLayout loc_ll;
    LinearLayout alarm_ll;
    LinearLayout transport_ll;


    DatePickerDialog dateDialog;
    TimePickerDialog startTimeDialog;
    TimePickerDialog endTimeDialog;


    Calendar startTime;
    Calendar endTime;
    Calendar date;


    private Menu menu;

    final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    Calendar currentDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event_activity);


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.ee_toolbar);
        setSupportActionBar(toolbar);


        currentDate = Calendar.getInstance();

        Intent inIntent = getIntent();
        Bundle inBundle = inIntent.getExtras();

        name = inBundle.getString("name");
        location = inBundle.getString("location");
        id = inBundle.getLong("id");
//        Color inColor = (Color)inBundle.getSerializable("color");
//        int typeColor = Color.parseColor(inColor.toString());
//        if(typeColor == getColor(R.color.foodType)){
//            type = "food";
//        }else if(typeColor == getColor(R.color.shopType)){
//            type = "shopping";
//        }else if(typeColor == getColor(R.color.sightseeingType)){
//            type = "sightseeing";
//        }else{
//            type = "none";
//        }
        type ="";
//
//        type = inBundle.getString("interest").toLowerCase();
        final GregorianCalendar startDate = (GregorianCalendar) inBundle.getSerializable("startTime");
        final GregorianCalendar endDate = (GregorianCalendar) inBundle.getSerializable("endTime");

        if (name.equals("") && location.equals(""))
            new_event = true;
        else
            new_event = false;

        if(new_event)
            getSupportActionBar().setTitle("Add an Event");
        else
            getSupportActionBar().setTitle("Edit an Event");


        date = (GregorianCalendar) startDate.clone();
        startTime =  (GregorianCalendar) startDate.clone();
        endTime =  (GregorianCalendar) endDate.clone();

        title_tv = (TextView) findViewById(R.id.ee_title_tv);
        type_iv = (ImageView) findViewById(R.id.ee_type_iv);

        title_tv.setText(name);
        switch(type){
            case "shopping":
                type_iv.setImageResource(R.drawable.shopping);
                break;
            case "sightseeing":
                type_iv.setImageResource(R.drawable.shopping);
                break;
            case "food":
                type_iv.setImageResource(R.drawable.food);
                break;
            default:
                type_iv.setImageResource(R.drawable.more);
                break;
        }


        loc_ll = (LinearLayout) findViewById(R.id.ee_location);
        alarm_ll = (LinearLayout) findViewById(R.id.ee_alarm);
        date_ll = (LinearLayout) findViewById(R.id.ee_date);
        date_tv = (TextView) findViewById(R.id.ee_date_tv);
        time_ll = (LinearLayout) findViewById(R.id.ee_time);
        startTime_tv = (TextView) findViewById(R.id.ee_start_time_tv);
        endTime_tv = (TextView) findViewById(R.id.ee_end_time_tv);

        indays_tv = (TextView) findViewById(R.id.ee_indays_tv);

        sdf = new SimpleDateFormat("MMM dd", Locale.US);


        date_tv.setText(days[date.get(Calendar.DAY_OF_WEEK) - 1] + ", " + sdf.format(date.getTime()));

        String startampm = "am";
        int startHour = startTime.get(Calendar.HOUR);
        if(startHour>12) {
            startHour -= 12;
            startampm = "pm";
        }
        int startMinute = startTime.get(Calendar.MINUTE);
        String startMinuteStr = Integer.toString(startMinute);
        if(startMinute<10){
            startMinuteStr = "0"+ startMinuteStr;
        }
        startTime_tv.setText(startHour + ":" + startMinuteStr + startampm);

        String endampm = "am";
        int endHour = endTime.get(Calendar.HOUR);
        if(endHour>12) {
            endHour -= 12;
            endampm = "pm";
        }
        int endMinute = endTime.get(Calendar.MINUTE);
        String endMinuteStr = Integer.toString(startMinute);
        if(endMinute<10){
            endMinuteStr = "0"+ endMinuteStr;
        }
        endTime_tv.setText(endHour + ":" + endMinuteStr + endampm);


        long daysBetween = getDateDiff(currentDate.getTime(), date.getTime(), TimeUnit.DAYS);
        indays_tv.setText("In "+Math.round(daysBetween)+" days");
        transport_ll = (LinearLayout) findViewById(R.id.ee_transportation);



        date_ll.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                GregorianCalendar newDate = new GregorianCalendar();
                newDate.set(year, monthOfYear, dayOfMonth);
                date_tv.setText(days[newDate.get(Calendar.DAY_OF_WEEK) - 1] + ", " + sdf.format(newDate.getTime()));
                startTime.set(year, monthOfYear, dayOfMonth);
                endTime.set(year, monthOfYear, dayOfMonth);
                date.set(year, monthOfYear, dayOfMonth);

                long daysBetween = getDateDiff(currentDate.getTime(),date.getTime(),TimeUnit.DAYS);
                indays_tv.setText("In " + Math.round(daysBetween) + " days");

                if(startTime.compareTo(startDate) != 0 || endTime.compareTo(endDate) != 0){
                    editDone(true);
                }else{
                    editDone(false);
                }
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
                String selMin = Integer.toString(selectedMinute);
                if(selectedMinute<10){
                    selMin = "0"+selectedMinute;
                }
                startTime_tv.setText(selectedHour + ":" + selMin + ampm);

                startTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH),startTime.get(Calendar.DAY_OF_MONTH), selectedHour, selectedMinute);

                if(startTime.compareTo(startDate) != 0 || endTime.compareTo(endDate) != 0){
                    editDone(true);
                }else{
                    editDone(false);
                }
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

                if(endTime.before(startTime))
                {
                    Toast.makeText(EditEventActivity.this, "End time must be after the starting time" ,
                            Toast.LENGTH_SHORT).show();
                    selectedHour = startTime.HOUR_OF_DAY + 1;
                    selectedMinute = startTime.MINUTE;
                }
                String selMin = Integer.toString(selectedMinute);
                if(selectedMinute<10){
                    selMin = "0"+selectedMinute;
                }
                endTime_tv.setText(selectedHour + ":" + selMin + ampm);
                endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH),endTime.get(Calendar.DAY_OF_MONTH), selectedHour, selectedMinute);

                if(startTime.compareTo(startDate) != 0 || endTime.compareTo(endDate) != 0){
                    editDone(true);
                }else{
                    editDone(false);
                }
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

    public void editDone(boolean i){
        if(i){
            menu.findItem(R.id.action_save).setVisible(true);
            menu.findItem(R.id.action_delete).setVisible(false);
        }else{
            menu.findItem(R.id.action_save).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(true);
        }
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
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
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

        if (id == R.id.action_save) {

            if(new_event)
            {
                Toast.makeText(EditEventActivity.this, "No Adding Trips at this moment. Sorry!" ,
                        Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(EditEventActivity.this, "Trip Created!" ,
                        Toast.LENGTH_SHORT).show();
                //SOmething here
            }

        }
        else if(id == R.id.action_cancel){
            finish();
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
