package buzztrapp.trapp.manage_trips;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import buzztrapp.trapp.R;
import buzztrapp.trapp.edit_trip.EditTripActivity;
import buzztrapp.trapp.edit_trip.TripItem;
import buzztrapp.trapp.helper.NextLocationAlertReceiver;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Aaron on 3/24/2016.
 */
public class ManageTripsFragment extends Fragment{


    View noTrippImage;
    View noTrippText;
    RecyclerView rv;

    private List<Trip> fullTripsList = new ArrayList<Trip>();
    private ManageTripsRVAdapter adapter;
    private View mProgressView;
    private View mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("Aaron", "Fragment: onCreateView()");
        return inflater.inflate(R.layout.manage_trips_content_frag, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getTrips();
        mProgressView = (View) getActivity().findViewById(R.id.manage_trip_progress);
        mListView = (View) getActivity().findViewById(R.id.drawer_listview);
        Log.d("ManageTrip", "Fragment: onActivityCreated()");
        Log.d("ManageTrip", "full trip list size() = " + fullTripsList.size());


    }

    private void getTrips() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", preferences.getString("token", ""));
        RequestParams params = new RequestParams();
        client.get("http://173.236.255.240/api/myTrips", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String json = new String(response);
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    Log.d("ManageTrip", "Got back " + jsonArray.length() + " trips");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        GregorianCalendar startDate = new GregorianCalendar();
                        startDate.setTime(sdf.parse(jsonObject.getString("startDate")));
                        GregorianCalendar endDate = new GregorianCalendar();
                        endDate.setTime(sdf.parse(jsonObject.getString("endDate")));

                        String image_name = jsonObject.getString("location");
                        String locn = image_name;
                        image_name = image_name.replaceAll("[^A-Za-z]+", "").toLowerCase();
                        image_name = "@drawable/" + image_name;

                        if (i == 0) {
                            // Testing: Send a notification 5 seconds from now reminding of the next trip----------------------s
                            // Define a time value of 5 seconds
                            Long alertTime = new GregorianCalendar().getTimeInMillis() + 5 * 1000;

                            // Define our intention of executing AlertReceiver
                            Intent alertIntent = new Intent(getActivity(), NextLocationAlertReceiver.class);
                            alertIntent.putExtra("location", locn);

                            // Allows you to schedule for your application to do something at a later date
                            // even if it is in he background or isn't active
                            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                            // set() schedules an alarm to trigger
                            // Trigger for alertIntent to fire in 5 seconds
                            // FLAG_UPDATE_CURRENT : Update the Intent if active
                            alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                                    PendingIntent.getBroadcast(getActivity(), 1, alertIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT));
                        }
                        //-------------------------------------------------------

                        int image_resrc = getResources().getIdentifier(image_name, null, getActivity().getPackageName());
                        Log.d("ManageTrip", "image_resource = " + image_resrc + ", image_name = " + image_name);
                        Trip trip = new Trip(jsonObject.getString("_id"), jsonObject.getString("location"), startDate, endDate, image_resrc);
                        fullTripsList.add(trip);
                    }

                    if (fullTripsList.size() != 0) {
                        noTrippImage = getActivity().findViewById(R.id.noTripsImage);
                        noTrippText = getActivity().findViewById(R.id.noTripsText);
                        ((ViewGroup) noTrippImage.getParent()).removeView(noTrippImage);
                        ((ViewGroup) noTrippText.getParent()).removeView(noTrippText);

                        rv = (RecyclerView) getActivity().findViewById(R.id.rv);
                        rv.setHasFixedSize(true);

                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        rv.setLayoutManager(llm);

                        adapter = new ManageTripsRVAdapter(fullTripsList);
                        rv.setAdapter(adapter);
                        rv.addOnItemTouchListener(
                                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        // TODO Handle item click
                                        Toast.makeText(getActivity(), "rv clicked = " + position,
                                                Toast.LENGTH_SHORT).show();
                                        showProgress(true);
                                        retrieveTripItems(position);
                                    }
                                })
                        );

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d("ManageTrip", "Inside failure");
            }
        });
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mListView.setVisibility(show ? View.GONE : View.VISIBLE);
            mListView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mListView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void retrieveTripItems (final int position) {
        final ArrayList<TripItem> tripItems = new ArrayList<TripItem>();
        SharedPreferences preferences = this.getActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", preferences.getString("token", ""));
        RequestParams params = new RequestParams();
        client.get("http://173.236.255.240/api/myTripItems?tripId="+fullTripsList.get(position).id, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String json = new String(response);
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        GregorianCalendar startDate = new GregorianCalendar();
                        startDate.setTime(sdf.parse(jsonObject.getString("startTime")));
                        GregorianCalendar endDate = new GregorianCalendar();
                        endDate.setTime(sdf.parse(jsonObject.getString("endTime")));
                        Log.d("ManageTrip", "trip id - " + jsonObject.getString("_id"));
                        Log.d("ManageTrip", "start time - " + sdf.parse(jsonObject.getString("startTime")));
                        Log.d("ManageTrip", "end time - " + sdf.parse(jsonObject.getString("endTime")));
                        TripItem tripItem = new TripItem(jsonObject.getString("_id"), jsonObject.getString("tripId"),
                                startDate, endDate, jsonObject.getString("city"), jsonObject.getString("interest"),
                                jsonObject.getInt("averageTime"), jsonObject.getString("description"), jsonObject.getString("address"),
                                jsonObject.getString("name"));

                        tripItems.add(tripItem);

                    }
                    showProgress(false);
                    Intent intent = new Intent(getActivity(), EditTripActivity.class);
                    Bundle bundle = new Bundle();

                    Log.d("NISHEETH", fullTripsList.get(position).startDate.getTime().toString());
                    bundle.putSerializable("startDate", fullTripsList.get(position).startDate.getTime());
                    bundle.putSerializable("endDate", fullTripsList.get(position).endDate.getTime());
                    bundle.putString("id", fullTripsList.get(position).id);
                    bundle.putSerializable("tripItems", tripItems);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d("EditTrip", "Inside failure");
            }
        });
    }

}