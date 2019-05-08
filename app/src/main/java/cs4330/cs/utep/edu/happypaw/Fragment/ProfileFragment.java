package cs4330.cs.utep.edu.happypaw.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import cs4330.cs.utep.edu.happypaw.R;
import cs4330.cs.utep.edu.happypaw.Adapter.TabFragmentPagerAdapter;


public class ProfileFragment extends Fragment {

    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ctx = getActivity();

        TabLayout tabLayout = rootView.findViewById(R.id.tablayout);
        ViewPager pager = rootView.findViewById(R.id.viewpager);

        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getActivity(), getChildFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Trips"));
        tabLayout.addTab(tabLayout.newTab().setText("Vet Visits"));
        tabLayout.setupWithViewPager(pager);

        setHasOptionsMenu(true);
        return rootView;
    }

}
