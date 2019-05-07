package cs4330.cs.utep.edu.happypaw.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toolbar;

import cs4330.cs.utep.edu.happypaw.PushNotificationHandler;
import cs4330.cs.utep.edu.happypaw.R;
import cs4330.cs.utep.edu.happypaw.TabFragmentPagerAdapter;


public class ProfileFragment extends Fragment {

    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ctx = getActivity();

        TabLayout tabLayout1 = rootView.findViewById(R.id.tablayout);
        ViewPager pager = rootView.findViewById(R.id.viewpager);
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getActivity(), getChildFragmentManager());
        tabLayout1.addTab(tabLayout1.newTab().setText("Trips"));
        tabLayout1.addTab(tabLayout1.newTab().setText("Vet Visits"));


//        // Find the view pager that will allow the user to swipe between fragments
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//
//        // Create an adapter that knows which fragment should be shown on each page
//        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());
//
//        // Set the adapter onto the view pager
//        viewPager.setAdapter(adapter);
//
//        // Give the TabLayout the ViewPager
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }
}
