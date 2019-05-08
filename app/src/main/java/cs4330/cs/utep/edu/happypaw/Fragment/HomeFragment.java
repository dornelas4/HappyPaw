package cs4330.cs.utep.edu.happypaw.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.Timer;
import java.util.TimerTask;

import cs4330.cs.utep.edu.happypaw.Model.FeedTimer;
import cs4330.cs.utep.edu.happypaw.Model.FoodContainer;
import cs4330.cs.utep.edu.happypaw.Model.Schedule;
import cs4330.cs.utep.edu.happypaw.R;

import cs4330.cs.utep.edu.happypaw.Helper.SchedulerClient;

import cs4330.cs.utep.edu.happypaw.Util.TimeUtil;


/**
 * A simple {@link Fragment} subclass.

 */
public class HomeFragment extends Fragment {
    final static float ARC_BOTTOM_TEXT_SIZE = 35.0f;
    final static String TAG = "HomeFragment";

    SchedulerClient scheduler;
    ArcProgress foodContainer;
    Timer timerThread;
    FeedTimer timer;
    View view;
    int mealPday = 0;
    int currMeal = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        scheduler = new SchedulerClient();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        timer = new FeedTimer();
        setUpProgressBar(view);
//        setUpNextFeedingTextView(view);
        Button setTimerBtn =  view.findViewById(R.id.btn_set_timer);
        Button feedBtn = view.findViewById(R.id.btn_feed_now);

        setTimerBtn.setOnClickListener((v) -> {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.setTargetFragment(this, 1);
            newFragment.show(getFragmentManager(), "timePicker");
        });

        feedBtn.setOnClickListener((v) -> {
            SchedulerClient client = new SchedulerClient();
            client.feed(new SchedulerClient.SchedulerListener<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.i(TAG, "Feed");
                }

                @Override
                public void onError(String msg) {
                    Log.i(TAG, "could not feed");

                }
            });
        });
        return view;
    }

    /**
     * Sets the progress bar with the food container percentage returned
     * from a server call
     *
     * @param view - HomeFragment view to populate percentage
     */
    public void setUpProgressBar(View view){
        foodContainer = view.findViewById(R.id.arc_progress);
        foodContainer.setBottomTextSize(ARC_BOTTOM_TEXT_SIZE);
        Activity ctx = getActivity();
        scheduler.getProgress(new SchedulerClient.SchedulerListener<FoodContainer>() {
            @Override
            public void onSuccess(FoodContainer result) {
                ctx.runOnUiThread(() -> {
                    foodContainer.setProgress(result.getPercentage());
                    foodContainer.setBottomText("Capacity: "+result.getCapacity()+"L");
                });
                setUpNextFeedingTextView(view);
            }
            @Override
            public void onError(String msg) {
                showToastOnUiThread(msg);
            }
        });
    }

    /**
     *
     * Sets timer with the next feeding schedule returned
     * from a server call
     *
     * @param view - HomeFragment view to populate timer
     */
    public void setUpNextFeedingTextView(View view){
        TextView mealPerDay = view.findViewById(R.id.meal_per_day_text);
        Activity actContext = getActivity();
        scheduler.getSchedule(new SchedulerClient.SchedulerListener<Schedule>() {
            @Override
            public void onSuccess(Schedule result) {

                actContext.runOnUiThread(()->{
                    mealPday = result.getMealPerDay();
                    mealPerDay.setText(currMeal+"/"+mealPday);
                });

                startTimer(result.getNextFeedTime());
            }

            @Override
            public void onError(String msg) {
                showToastOnUiThread(msg);
            }
        });
    }
    static boolean flag = false;
    /**
     * Starts a count down timer that shows how long till the next
     * feeding time
     *
     * @param time - next feeding time in milliseconds
     */
    public void startTimer(long time){
        TextView nextFeeding = getView().findViewById(R.id.text_view_next_feeding);
        TextView mealPerDay = view.findViewById(R.id.meal_per_day_text);
        timerThread =  new Timer();
        timer.start(time);
        Activity ctxAct = getActivity();
        timerThread.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ctxAct.runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        if (timer.elapsedTime() == 0){
                            if (!flag) {
                                mealPerDay.setText((currMeal + 1) + "/" + mealPday);
                            }
                            setUpNextFeedingTextView(getView());
                            flag = true;
                            return;
                        }
                        nextFeeding.setText( TimeUtil.formatElapsedTime(timer.elapsedTime()));
                    }
                });
            }
        }, 0, 200);
    }

    /**
     * Sets the dog feeding schedule using the Scheduler API
     * @param mealPerDay - meals per day
     * @param firstMealHour - hour of the first meal
     * @param firstMealMinute - minute of the first meal
     * @param intervalHour - interval
     * @param intervalMinute
     */
    public void setSchedule(int mealPerDay, int firstMealHour, int firstMealMinute,
                            int intervalHour, int intervalMinute){
        Schedule schedule = new Schedule(mealPerDay, firstMealHour, firstMealMinute,
                                         intervalHour, intervalMinute);
        String json = schedule.toJson();
        scheduler.setSchedule(json, new SchedulerClient.SchedulerListener<String>() {
            @Override
            public void onSuccess(String result) {
                showToastOnUiThread(result);
                setUpNextFeedingTextView(view);
            }

            @Override
            public void onError(String msg) {
                showToastOnUiThread(msg);
            }
        });
    }

    public void showToastOnUiThread(String msg){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (timerThread != null){
            timerThread.cancel();
            timerThread.purge();
        }
        Log.d("Lifecycle", "Destroy");

    }

    public static class TimePickerFragment extends DialogFragment {

        HomeFragment ctx;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            try {
                ctx = (HomeFragment) getTargetFragment();
            } catch (ClassCastException e) {
                throw new ClassCastException("Fragment must be HomeFragment");
            }
        }

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
            times.setMaxValue(5);
            times.setMinValue(1);

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

                            ctx.setSchedule(mealPerDay, firstMealHour, firstMealMinute,
                                    intervalHour, intervalMinute);

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TimePickerFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();

        }
    }
}
