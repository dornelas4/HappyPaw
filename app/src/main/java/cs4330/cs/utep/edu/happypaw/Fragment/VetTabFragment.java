package cs4330.cs.utep.edu.happypaw.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import cs4330.cs.utep.edu.happypaw.Adapter.VetVisitAdapter;
import cs4330.cs.utep.edu.happypaw.R;
import cs4330.cs.utep.edu.happypaw.Util.TimeUtil;


public class VetTabFragment extends Fragment {

    Context ctx;
    VetVisitAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_vet_tab, container, false);
        ctx = getActivity();
        adapter = new VetVisitAdapter(ctx);

        RecyclerView rcVet = root.findViewById(R.id.recycler_vet);
        rcVet.setLayoutManager(new LinearLayoutManager(ctx));
        rcVet.setAdapter(adapter);

        setHasOptionsMenu(true);
        return root;
    }

    private void datePicker(){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int currYear = c.get(Calendar.YEAR);
        int currMonth = c.get(Calendar.MONTH);
        int currDay = c.get(Calendar.DAY_OF_MONTH);
        VetTabFragment ctxFrag = this;

        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, R.style.TimePickerTheme,
                (view, year, monthOfYear, dayOfMonth) -> ctxFrag.tiemPicker(year, monthOfYear, dayOfMonth), currYear, currMonth, currDay);
        datePickerDialog.show();
    }

    private void tiemPicker(int year, int month, int day){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int currHour = c.get(Calendar.HOUR_OF_DAY);
        int currMinute = c.get(Calendar.MINUTE);
        VetTabFragment ctxFrag = this;

        // Launch Time Picker Dialog
//        TimePickerDialog timePicker = new TimePickerDialog(mContext, R.style.TimePickerTheme, fromListener, hour, min, false);
        TimePickerDialog timePickerDialog = new TimePickerDialog(ctx, R.style.TimePickerTheme,
                (view, hourOfDay, minute) -> ctxFrag.showDialog(year, month, day, hourOfDay, minute), currHour, currMinute, false);
        timePickerDialog.show();
    }

    public void showDialog(int year, int month, int day, int hour, int minute){
        FragmentManager fm = getChildFragmentManager();
        LayoutInflater inflater = LayoutInflater.from(ctx);

        final View dialogView = inflater.inflate(R.layout.dialog_add, null);
        final EditText etDoctor = dialogView.findViewById(R.id.doctor_name);
        final EditText etReason = dialogView.findViewById(R.id.reason);

        final AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle("Enter vet visit information")
                .setView(dialogView)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                            String doctor = etDoctor.getText().toString();
                            String reason = etReason.getText().toString();

                            String dateString = String.format("%d-%02d-%02d %02d:%02d:00", year, month, day, hour, minute);
                            Date date = TimeUtil.str2Date(dateString);
                            adapter.addVetVisit(doctor, reason, date);
                            dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_add:
                datePicker();
//                showDialog();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
