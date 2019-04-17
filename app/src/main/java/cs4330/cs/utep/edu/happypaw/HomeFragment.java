package cs4330.cs.utep.edu.happypaw;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.

 */
public class HomeFragment extends Fragment {
    SchedulerClient testApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        testApi = new SchedulerClient();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment

        Button setTimerBtn =  view.findViewById(R.id.btn_set_timer);

        setTimerBtn.setOnClickListener((v) -> {

            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getFragmentManager(), "timePicker");

        });


        return view;
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        SchedulerClient testApi = new SchedulerClient();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_time_picker, null);

            TimePicker firstMeal = view.findViewById(R.id.first_time_picker);
            TimePicker interval = view.findViewById(R.id.intreval_time_picker);
            NumberPicker times = view.findViewById(R.id.times);

            interval.setIs24HourView(true);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view).setTitle("Set Time")
                    // Add action buttons
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            int firstMealHour, firstMealMinute, intervalHour, intervalMinute;
                            int mealPerDay = times.getValue();

                            if(Build.VERSION.SDK_INT < 23){
                                firstMealHour = firstMeal.getCurrentHour();
                                firstMealMinute = firstMeal.getCurrentMinute();
                                intervalHour = interval.getCurrentHour();
                                intervalMinute = interval.getCurrentMinute();
                            }
                            else {
                                firstMealHour = firstMeal.getHour();
                                firstMealMinute = firstMeal.getMinute();
                                intervalHour = interval.getHour();
                                intervalMinute = interval.getMinute();
                            }

                            new Thread(new Runnable() {
                                public void run() {
                                    // a potentially time consuming task
                                    try {
                                        Log.d("Api call",testApi.doPostRequest("http://172.19.158.46:5000/api/people",""));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TimePickerFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();

        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }



}
