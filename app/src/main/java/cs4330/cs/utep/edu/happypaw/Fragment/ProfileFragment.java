package cs4330.cs.utep.edu.happypaw.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toolbar;

import cs4330.cs.utep.edu.happypaw.PushNotificationHandler;
import cs4330.cs.utep.edu.happypaw.R;


public class ProfileFragment extends Fragment {

    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ctx = getActivity();

        TabLayout tabLayout1 = rootView.findViewById(R.id.tablayout1);



        tabLayout1.addTab(tabLayout1.newTab().setText("Trips"));
        tabLayout1.addTab(tabLayout1.newTab().setText("Vet Visits"));
        
        return rootView;
    }
}
