package cs4330.cs.utep.edu.happypaw;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.gms.maps.SupportMapFragment;


public class ProfileFragment extends Fragment {

    Toolbar toolbar;
    private boolean isChecked = false;
    ImageButton like,back;
    Button poke;
    ImageView facebook,instagram,follow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        return rootView;
    }



}
