package buzztrapp.trapp.helper;

/**
 * Created by nishe on 20-Apr-16.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.Console;

import buzztrapp.trapp.R;
import buzztrapp.trapp.edit_trip.EditTripActivity;
import buzztrapp.trapp.manage_trips.ManageTripsActivity;

public class NextLocationAlertReceiver  extends BroadcastReceiver {
    String location;
    String address;
    // Called when a broadcast is made targeting this class
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras == null) {
            location = null;
            address = null;
        }
        else {
            location = extras.getString("next_item");
            address = extras.getString("item_address");
        }

        // get weather for the location


        createNotification(context, "Next Location",
                "It's time to leave for " + location,
                "Time to leave!");

    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert){

        Uri mapaddress = Uri.parse("geo:0,0?q="+address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapaddress);

        Intent edittripIntent = new Intent(context, EditTripActivity.class);

        // Define an Intent and an action to perform with it by another application
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0, mapIntent, 0);
        PendingIntent editIntent = PendingIntent.getActivity(context, 0, edittripIntent, 0);
        PendingIntent manageIntent = PendingIntent.getActivity(context, 0, new Intent(context, ManageTripsActivity.class), 0);


        // Builds a notification
        Bitmap largeico = BitmapFactory.decodeResource(context.getResources(), R.drawable.trapp_icon_1);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.trapp_icon_1)
                        .setLargeIcon(largeico)
                        .setContentTitle(msg)
                        .setTicker(msgAlert)
                        .setContentText(msgText);

        mBuilder.addAction(R.drawable.ic_create_black_24dp, "Edit", editIntent);
        mBuilder.addAction(R.drawable.ic_snooze_black_24dp, "Snooze", manageIntent);

        // Defines the Intent to fire when the notification is clicked
        mBuilder.setContentIntent(notificIntent);

        // Set the default notification option
        // DEFAULT_SOUND : Make sound
        // DEFAULT_VIBRATE : Vibrate
        // DEFAULT_LIGHTS : Use the default light notification
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        // Auto cancels the notification when clicked on in the task bar
        mBuilder.setAutoCancel(true);

        // Gets a NotificationManager which is used to notify the user of the background event
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Post the notification
        mNotificationManager.notify(1, mBuilder.build());


    }
}


