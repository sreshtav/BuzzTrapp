package buzztrapp.trapp.create_trip;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Created by Aaron on 4/1/2016.
 */
public class CreateTripPagerAdapter extends FragmentPagerAdapter{

    public CreateTripPagerAdapter(android.support.v4.app.FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){

            case 0:

                fragment = new CreateTripDestinationFragment();

                break;

            case 1:

                fragment = new CreateTripDurationFragment();

                break;

        }

        return fragment;
    }


    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();

        switch (position) {

            case 0:

                return "Destination";

            case 1:

                return "Duration";
            default:
                return null;
        }
    }
}
