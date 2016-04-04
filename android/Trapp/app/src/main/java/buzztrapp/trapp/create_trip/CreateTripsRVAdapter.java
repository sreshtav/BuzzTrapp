package buzztrapp.trapp.create_trip;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import buzztrapp.trapp.R;
import buzztrapp.trapp.create_trip.helper.ItemTouchHelperAdapter;
import buzztrapp.trapp.create_trip.helper.OnStartDragListener;

/**
 * Created by Aaron on 4/2/2016.
 */
public class CreateTripsRVAdapter extends RecyclerView.Adapter<CreateTripsRVAdapter.DestViewHolder> implements ItemTouchHelperAdapter{

    private final OnStartDragListener mDragStartListener;
    Context context;

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

    CreateTripsRVAdapter(Context context, List<Destination> destinations, OnStartDragListener dragStartListener){
        this.destinations = destinations;
        mDragStartListener = dragStartListener;
        this.context = context;
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
    public void onBindViewHolder(final DestViewHolder destViewHolder, int i) {

        destViewHolder.desc.setText(destinations.get(i).location);
        destViewHolder.image.setImageResource(destinations.get(i).imageId);

        destViewHolder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDragStartListener.onStartDrag(destViewHolder);

                return false;
            }
        });
    }



    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(destinations, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(destinations, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        destinations.remove(position);
        notifyItemRemoved(position);
        if (context instanceof CreateTripActivity){
            ((CreateTripActivity)context).setDestinations(destinations);
        }
    }
//    Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return destinations.size();
    }

    public List<Destination> getDestinations(){
        return destinations;
    }

}
