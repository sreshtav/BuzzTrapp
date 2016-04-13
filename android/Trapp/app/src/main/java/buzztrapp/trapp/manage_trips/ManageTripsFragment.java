package buzztrapp.trapp.manage_trips;

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
import java.util.GregorianCalendar;
import java.util.List;

import buzztrapp.trapp.R;
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

        Log.d("ManageTrip", "Fragment: onActivityCreated()");
        Log.d("ManageTrip", "full trip list size() = "+ fullTripsList.size());



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
                        image_name = image_name.replaceAll("[^A-Za-z]+", "").toLowerCase();
                        image_name = "@drawable/"+image_name;

                        int image_resrc = getResources().getIdentifier(image_name, null, getActivity().getPackageName());
                        Log.d("ManageTrip", "image_resource = "+image_resrc+ ", image_name = "+image_name);
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