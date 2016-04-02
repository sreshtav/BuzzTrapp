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
 * Created by Aaron on 4/2/2016.
 */
public class CreateTripsRVAdapter extends RecyclerView.Adapter<CreateTripsRVAdapter.TripViewHolder>{

    public static class TripViewHolder extends RecyclerView.ViewHolder {

        CardView destView;
        TextView desc;
        ImageView image;

        TripViewHolder(View itemView) {
            super(itemView);
            destView = (CardView)itemView.findViewById(R.id.destView);
            desc = (TextView)itemView.findViewById(R.id.destDesc);
            image = (ImageView)itemView.findViewById(R.id.destImage);
        }
    }

    List<Destination> destinations;

    CreateTripsRVAdapter(List<Destination> destinations){
        this.destinations = destinations;
    }

    public void editTrips(List<Destination> destinations){
        this.destinations = destinations;
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
    public void onBindViewHolder(TripViewHolder destViewHolder, int i) {

        destViewHolder.desc.setText(destinations.get(i).location);
        destViewHolder.image.setImageResource(destinations.get(i).imageId);
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

}
