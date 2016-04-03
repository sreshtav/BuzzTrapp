package buzztrapp.trapp.manage_trips;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import buzztrapp.trapp.R;

/**
 * Created by Aaron on 3/6/2016.
 */
public class ManageTripsRVAdapter extends RecyclerView.Adapter<ManageTripsRVAdapter.TripViewHolder>{


    public static class TripViewHolder extends RecyclerView.ViewHolder {

        CardView tripView;
        TextView desc;
        ImageView image;

        TripViewHolder(View itemView) {
            super(itemView);
            tripView = (CardView)itemView.findViewById(R.id.tripView);
            desc = (TextView)itemView.findViewById(R.id.tripDesc);
            image = (ImageView)itemView.findViewById(R.id.tripImage);
        }
    }

    List<Trip> trips;

   ManageTripsRVAdapter(List<Trip> trips){
        this.trips = trips;
    }

    public void editTrips(List<Trip> trips){
        this.trips = trips;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.manage_trips_individual, viewGroup, false);
        TripViewHolder pvh = new TripViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TripViewHolder tripViewHolder, int i) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd yyyy");

        GregorianCalendar startDate = trips.get(i).startDate;
        GregorianCalendar endDate = trips.get(i).endDate;

        String startMth = getMonthForInt(startDate.get(Calendar.MONTH)).substring(0,3);
        String endMth = getMonthForInt(endDate.get(Calendar.MONTH)).substring(0,3);

        int startDay = startDate.get(Calendar.DAY_OF_MONTH);
        int endDay = endDate.get(Calendar.DAY_OF_MONTH);

        int startYear = startDate.get(Calendar.YEAR);
        int endYear = endDate.get(Calendar.YEAR);

        String startDateString = startMth +" " + startDay + ", " + startYear;
        String endDateString = endMth +" " + endDay + ", " + endYear;

        tripViewHolder.desc.setText(trips.get(i).location + " | " + startDateString + " - " + endDateString);
        tripViewHolder.image.setImageResource(trips.get(i).imageId);
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

}
