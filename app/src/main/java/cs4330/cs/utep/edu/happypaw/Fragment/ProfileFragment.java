package cs4330.cs.utep.edu.happypaw.Fragment;


import android.content.Context;
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

import cs4330.cs.utep.edu.happypaw.PushNotificationHandler;
import cs4330.cs.utep.edu.happypaw.R;


public class ProfileFragment extends Fragment {

    Toolbar toolbar;
    private boolean isChecked = false;
    ImageButton like,back;
    Button poke;
    ImageView facebook,instagram,follow;
    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ctx = rootView.getContext();
        Button btn = rootView.findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushNotificationHandler pnf = new PushNotificationHandler(ctx);
                pnf.showNotifications("Test","Message");
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        insertNestedFragment();
    }

    public void insertNestedFragment(){
        Fragment childFragment = new VaccineFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container,childFragment).commit();

        Fragment second = new VaccineFragment();
        transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.second_container,second).commit();
    }




}
