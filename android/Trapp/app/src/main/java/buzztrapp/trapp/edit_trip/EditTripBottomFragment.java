package buzztrapp.trapp.edit_trip;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import buzztrapp.trapp.R;
import buzztrapp.trapp.create_trip.CreateTripActivity;

/**
 * Created by Aaron on 4/12/2016.
 */
@TargetApi(23)
public class EditTripBottomFragment extends Fragment implements WeekView.EventClickListener, WeekView.EmptyViewClickListener, MonthLoader.MonthChangeListener{


    private String tripid;
    private TextView testTV;

    private List<TripItem> tripItems;

    private List<String> eventTypes;

    GregorianCalendar date;

    WeekView mWeekView;
    WeekViewEvent event;
    ArrayList<WeekViewEvent> events = new ArrayList<>();


    WeekView.EventClickListener mEventClickListener;
    MonthLoader.MonthChangeListener mMonthChangeListener;
    WeekView.EventLongPressListener mEventLongPressListener;

    private List<WeekViewEvent> mEventModels = new ArrayList<WeekViewEvent>();

    private WeekViewLoader weekViewLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.edit_trip_bottom_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventTypes = new ArrayList<>();
        date = new GregorianCalendar();
        date = ((EditTripActivity)getActivity()).getSelectedDate();
//        testTV = (TextView) getActivity().findViewById(R.id.textView6);
        tripid = ((EditTripActivity)getActivity()).getTripid();
//        testTV.setText("id = "+ tripid);

        tripItems = ((EditTripActivity)getActivity()).getTripItems();


        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) getActivity().findViewById(R.id.weekView);
// Set an action when any event is clicked.
        
        for (int i = 0; i<tripItems.size(); i++){
            event = new WeekViewEvent(i, tripItems.get(i).name, tripItems.get(i).startTime, tripItems.get(i).endTime);
            switch(tripItems.get(i).interest.toLowerCase()){
                case "food": event.setColor(getContext().getColor(R.color.foodType));
                    break;
                case "shopping": event.setColor(getContext().getColor(R.color.shopType));
                    break;
                case "sightseeing": event.setColor(getContext().getColor(R.color.sightseeingType));
                    break;
                default:
                    event.setColor(getContext().getColor(R.color.defaultType));
                    break;
            }
            events.add(event);
        }

        mEventClickListener = new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {

            }
        };

        mWeekView.setOnEventClickListener(mEventClickListener);

        weekViewLoader = new WeekViewLoader() {
            @Override
            public double toWeekViewPeriodIndex(Calendar instance) {
                return 0;
            }

            @Override
            public List<? extends WeekViewEvent> onLoad(int periodIndex) {
                /*List<WeekViewEvent> events = new ArrayList<>();

                for (int i = 0; i<tripItems.size(); i++){
                    event = new WeekViewEvent(i, tripItems.get(i).name, tripItems.get(i).startTime, tripItems.get(i).endTime);
                    events.add(event);
                }*/
                return events;
            }
        };

        mWeekView.setWeekViewLoader(weekViewLoader);

        weekViewLoader.onLoad((int)weekViewLoader.toWeekViewPeriodIndex(date));

        mWeekView.goToDate(date);

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
        /*MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                List<WeekViewEvent> events = new ArrayList<>();

                for (int i = 0; i<tripItems.size(); i++){
                    event = new WeekViewEvent(i, "id "+tripItems.get(i).id+" from "+tripItems.get(i).tripId, tripItems.get(i).startTime, tripItems.get(i).endTime);
                    events.add(event);
                }
                return events;
            }
        };*/
//        mWeekView.setMonthChangeListener(this);
        onMonthChange(2016,04);
        mWeekView.notifyDatasetChanged();

        mWeekView.setOnEventClickListener(this);
        mWeekView.setEmptyViewClickListener(this);
// Set long press listener for events.
        mWeekView.setEventLongPressListener(mEventLongPressListener);


    }

    public void changeDate(GregorianCalendar date){
        this.date = date;
//        testTV.setText("id = "+ tripid + ", SelectedDate = "+ this.date.getTime().toString());
        mWeekView.goToDate(date);
    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast toast = Toast.makeText(getContext(), event.getName() + " clicked!", Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = new Intent(getContext(), EditEventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", event.getName());
        bundle.putString("location", event.getLocation());
//        bundle.putSerializable("color", event.getColor());
        bundle.putSerializable("startTime", event.getStartTime());
        bundle.putSerializable("endTime", event.getEndTime());
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }

    @Override
    public void onEmptyViewClicked(Calendar time) {
        Toast toast = Toast.makeText(getContext(), "Empty event at time "+time.getTime().toString()+" clicked!", Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = new Intent(getContext(), EditEventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", "");
        bundle.putString("location", "");
//        bundle.putSerializable("color", event.getColor());
        bundle.putSerializable("startTime", time);
        Calendar endTime = (Calendar)time.clone();
        endTime.add(Calendar.HOUR, 1);
        bundle.putSerializable("endTime", endTime);
        intent.putExtras(bundle);
        getContext().startActivity(intent);

    }
   /* @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.

        return events;
    }*/

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with the events that was added by tapping on empty view.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        ArrayList<WeekViewEvent> newEvents = getNewEvents(newYear, newMonth);
        events.addAll(newEvents);
/*
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 4);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, newMonth-1);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setName("match foot");
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 4);
        startTime.set(Calendar.MINUTE, 20);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 5);
        endTime.set(Calendar.MINUTE, 0);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 2);
        endTime.set(Calendar.MONTH, newMonth-1);
        event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        startTime.add(Calendar.DATE, 1);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 15);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
        startTime.set(Calendar.HOUR_OF_DAY, 15);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        events.add(event);*/

        return events;
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }
   /**
     * Get events that were added by tapping on empty view.
     * @param year The year currently visible on the week view.
     * @param month The month currently visible on the week view.
     * @return The events of the given year and month.
     */
    private ArrayList<WeekViewEvent> getNewEvents(int year, int month) {

        // Get the starting point and ending point of the given month. We need this to find the
        // events of the given month.
        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(Calendar.YEAR, year);
        startOfMonth.set(Calendar.MONTH, month - 1);
        startOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        startOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        startOfMonth.set(Calendar.MINUTE, 0);
        startOfMonth.set(Calendar.SECOND, 0);
        startOfMonth.set(Calendar.MILLISECOND, 0);
        Calendar endOfMonth = (Calendar) startOfMonth.clone();
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getMaximum(Calendar.DAY_OF_MONTH));
        endOfMonth.set(Calendar.HOUR_OF_DAY, 23);
        endOfMonth.set(Calendar.MINUTE, 59);
        endOfMonth.set(Calendar.SECOND, 59);

        // Find the events that were added by tapping on empty view and that occurs in the given
        // time frame.
        ArrayList<WeekViewEvent> tempEvents = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : events) {
            if (event.getEndTime().getTimeInMillis() > startOfMonth.getTimeInMillis() &&
                    event.getStartTime().getTimeInMillis() < endOfMonth.getTimeInMillis()) {
                tempEvents.add(event);
            }
        }
        return tempEvents;
    }


/*
    public static void setmNewEvents(ArrayList<WeekViewEvent> mNewEvents) {
        EditTripBottomFragment.events = mNewEvents;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
