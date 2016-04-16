package buzztrapp.trapp.edit_trip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import buzztrapp.trapp.R;

public class EditEventActivity extends AppCompatActivity {

    private GregorianCalendar start = new GregorianCalendar();
    private GregorianCalendar end = new GregorianCalendar();

    private String name;
    private String location;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event_activity);

        Intent inIntent = getIntent();
        Bundle inBundle = inIntent.getExtras();

        name = inBundle.getString("name");
        location = inBundle.getString("location");
        GregorianCalendar startDate = (GregorianCalendar) inBundle.getSerializable("startTime");
        GregorianCalendar endDate = (GregorianCalendar) inBundle.getSerializable("endTime");

/*        TextView tv = (TextView)findViewById(R.id.ee_textView);

        if (name.equals("") && location.equals("")){
            tv.setText("No Event found! Create one now! Start time = "+startDate.getTime().toString() +", end time = "+endDate.getTime().toString());
        }
        else{
            tv.setText("Event details: name = "+name + ", location = "+location + ", start time = "+startDate.getTime().toString() +", end time = "+endDate.getTime().toString());
        }*/
    }
}
