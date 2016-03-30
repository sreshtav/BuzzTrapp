package buzztrapp.trapp;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Aaron on 3/24/2016.
 */
public class ManageTripsContentFrag extends Fragment{


    View noTrippImage;
    View noTrippText;
    RecyclerView rv;

    private List<Trip> trips;
    private List<Trip> fullTripsList = new ArrayList<Trip>();
    private ManageTripsRVAdapter adapter;

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

        Log.d("Aaron", "Fragment: onActivityCreated()");
        Log.d("Aaron", "full trip list size() = "+ fullTripsList.size());



    }

    public void addTrip(int noOfTrips)
    {
        if(noOfTrips == 1)
        {

            ((ViewGroup) noTrippImage.getParent()).removeView(noTrippImage);
            ((ViewGroup) noTrippText.getParent()).removeView(noTrippText);

            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(llm);
        }

        if(noOfTrips > 0)
        {
            initialiseData(noOfTrips);
            initialiseAdapter();

            adapter = new ManageTripsRVAdapter(trips);
            rv.setAdapter(adapter);
        }
    }

    private void initialiseData(int noOfTrips){
        int no = noOfTrips+1;
        trips = new ArrayList<>();
        fullTripsList = new ArrayList<>();

        fullTripsList.add(new Trip("New York", new GregorianCalendar(2015,12,03), new GregorianCalendar(2015,12,15), R.drawable.newyork));
        fullTripsList.add(new Trip("Singapore", new GregorianCalendar(2016, 01, 10), new GregorianCalendar(2016, 02, 03), R.drawable.singapore));
        fullTripsList.add(new Trip("Atlanta", new GregorianCalendar(2016,12,03), new GregorianCalendar(2016,12,15), R.drawable.atlanta));
        fullTripsList.add(new Trip("San Francisco", new GregorianCalendar(2016,8,04), new GregorianCalendar(2016,8,26), R.drawable.sanfrancisco));

        trips = fullTripsList.subList(0,no);
    }

    private void initialiseAdapter(){
    }

    private void getTrips() {
        Log.d("ManageTrip", "Inside getTrips");
        SharedPreferences preferences = this.getActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", preferences.getString("token", ""));
        RequestParams params = new RequestParams();
        client.get("http://173.236.255.240/api/myTrips", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("ManageTrip", "Inside success");
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
                        Trip trip = new Trip(jsonObject.getString("location"), startDate, endDate, R.drawable.newyork);
                        Log.d("ManageTrip", "Adding to list - " + trip.location + " " + trip.startDate + " " + trip.endDate);
                        fullTripsList.add(trip);


                        if (fullTripsList.size() != 0) {
                            Log.d("ManageTrip", "fulltriplist.size() = "+fullTripsList.size());
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
                        }
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
    


}