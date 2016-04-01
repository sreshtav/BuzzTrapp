package buzztrapp.trapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class CreateTripFragment extends Fragment {

//    DemoCollectionPagerAdapter demoCollectionPagerAdapter;
    ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("Aaron", "Fragment: onCreateView()");
        return inflater.inflate(R.layout.create_trip_fragment_activity, container, false);
    }
}
//
//
//public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter{
//    public DemoCollectionPagerAdapter(android.support.v4.app.FragmentManager fm){
//        super(fm);
//    }
//
//    @Override
//    public android.support.v4.app.Fragment getItem(int position) {
//        Fragment fragment = new CreateTripDestinationFragment();
//        Bundle args = new Bundle();
//        // Our object is just an integer :-P
////        args.putInt(CreateTripDestinationFragment.ARG_OBJECT, position + 1);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public int getCount() {
//        return 100;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position){
//        return "OBJECT " + (position + 1);
//    }
//}
//
