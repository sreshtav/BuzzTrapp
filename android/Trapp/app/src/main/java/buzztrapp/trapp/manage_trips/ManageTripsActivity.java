package buzztrapp.trapp.manage_trips;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.app.Notification;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;

import java.util.GregorianCalendar;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import buzztrapp.trapp.helper.NextLocationAlertReceiver;
import buzztrapp.trapp.Communicator;
import buzztrapp.trapp.R;
import buzztrapp.trapp.create_trip.CreateTripActivity;

public class ManageTripsActivity extends AppCompatActivity implements Communicator {

    private RecyclerView rv;
    private List<Trip> trips;
    private List<Trip> fullTripsList;
    private ManageTripsRVAdapter adapter;
    private int noOfTrips = 0;

    // Allows us to notify the user that something happened in the background
    NotificationManager notificationManager;

    // Used to track notifications
    int notifID = 8803;

    // Used to track if notification is active in the task bar
    boolean isNotificActive = false;

    ManageTripsFragment contentFragment;
    FragmentManager manager;
    FragmentTransaction transaction;

    private DrawerLayout drawerLayout;
    private ListView drawerlistView;

    private ActionBarDrawerToggle drawerListener;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_trips_activity);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.mt_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Manage Trips");
        Log.d("Aaron", "Main: onCreate and about to add contentFragment");




        contentFragment = new ManageTripsFragment();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.mt_body_layout, contentFragment, "ManageTripsFragment");
        transaction.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                noOfTrips++;
                noOfTrips = noOfTrips%4;
                Snackbar.make(view, "no of Trips = "+noOfTrips, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                contentFragment.addTrip(noOfTrips);*/

                Intent intent = new Intent(view.getContext(), CreateTripActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.mt_layout);
        drawerlistView = (ListView) findViewById(R.id.drawer_listview);


        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(ManageTripsActivity.this, "Drawer Closed",
                        Toast.LENGTH_SHORT).show();
                setAlarm();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(ManageTripsActivity.this, "Drawer Closed",
                        Toast.LENGTH_SHORT).show();
            }


        };
        drawerLayout.setDrawerListener(drawerListener);


    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }



    @Override
    public void respond(String data) {
    }

    public void setAlarm() {

        // Define a time value of 5 seconds
        Long alertTime = new GregorianCalendar().getTimeInMillis()+5*1000;

        // Define our intention of executing AlertReceiver
        Intent alertIntent = new Intent(this, NextLocationAlertReceiver.class);

        // Allows you to schedule for your application to do something at a later date
        // even if it is in he background or isn't active
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // set() schedules an alarm to trigger
        // Trigger for alertIntent to fire in 5 seconds
        // FLAG_UPDATE_CURRENT : Update the Intent if active
        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                PendingIntent.getBroadcast(this, 1, alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));

        Toast.makeText(ManageTripsActivity.this, "Alarm set", Toast.LENGTH_SHORT).show();
        Log.d("Nisheeth", "set the alarm");

    }

    public void showNotification() {

        // Define that we have the intention of opening MoreInfoNotification
        Intent moreInfoIntent = new Intent(this, ManageTripsActivity.class);

        // Used to stack tasks across activites so we go to the proper place when back is clicked
        TaskStackBuilder tStackBuilder = TaskStackBuilder.create(this);

        // Add all parents of this activity to the stack
        tStackBuilder.addParentStack(ManageTripsActivity.class);

        // Add our new Intent to the stack
        tStackBuilder.addNextIntent(moreInfoIntent);

        // Define an Intent and an action to perform with it by another application
        // FLAG_UPDATE_CURRENT : If the intent exists keep it but update it if needed
        PendingIntent pendingIntent = tStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Builds a notification
        Notification notificBuilder = new Notification.Builder(this)
                .setContentTitle("Stop by for Lunch?")
                .setContentText("Lunch")
                .setSmallIcon(R.drawable.trapp_icon_1)
                .setStyle(new Notification.BigTextStyle()
                        .bigText("A highly rated Indian restaurant Touch is 1.1 miles away. Tap for directions"))
                .addAction(R.drawable.trapp_icon_1_5, "Action button", pendingIntent)
                .setTicker("Stop by for Lunch?")
                .build();



        // Defines the Intent to fire when the notification is clicked
        //notificBuilder.setContentIntent(pendingIntent);

        // Gets a NotificationManager which is used to notify the user of the background event
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Post the notification
        notificationManager.notify(notifID, notificBuilder);

        // Used so that we can't stop a notification that has already been stopped
        isNotificActive = true;

    }

    public void stopNotification() {

        // If the notification is still active close it
        if(isNotificActive) {
            notificationManager.cancel(notifID);
        }

    }
}
