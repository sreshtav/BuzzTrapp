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
public class CreateTripsRVAdapter extends RecyclerView.Adapter<CreateTripsRVAdapter.DestViewHolder>{

    public static class DestViewHolder extends RecyclerView.ViewHolder {

        CardView destView;
        TextView desc;
        ImageView image;

        DestViewHolder(View itemView) {
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


//    Called by RecyclerView when it starts observing this Adapter
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


//    Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public DestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.create_trip_destination_individual, viewGroup, false);
        DestViewHolder pvh = new DestViewHolder(v);
        return pvh;
    }

//    Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(DestViewHolder destViewHolder, int i) {

        destViewHolder.desc.setText(destinations.get(i).location);
        destViewHolder.image.setImageResource(destinations.get(i).imageId);
    }

//    Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return destinations.size();
    }

}
