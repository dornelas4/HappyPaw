package cs4330.cs.utep.edu.happypaw;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.

 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment

        Button setTimerBtn =  view.findViewById(R.id.btn_set_timer);


        return view;
    }


}
