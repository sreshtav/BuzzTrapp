package buzztrapp.trapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.individual_manage_trips, viewGroup, false);
        TripViewHolder pvh = new TripViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TripViewHolder tripViewHolder, int i) {
        tripViewHolder.desc.setText(trips.get(i).name);
        tripViewHolder.image.setImageResource(trips.get(i).imageId);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

}
