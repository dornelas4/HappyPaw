package cs4330.cs.utep.edu.happypaw.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import cs4330.cs.utep.edu.happypaw.R;
import cs4330.cs.utep.edu.happypaw.VetVisitDBHelper;

public class VetVisitAdapter extends CursorRecyclerViewAdapter<VetVisitAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView reasonTextView;
        public TextView doctorPriceTextView;
        public TextView datePriceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            reasonTextView = itemView.findViewById(R.id.vet_reason);
            doctorPriceTextView = itemView.findViewById(R.id.vet_doctor);
            datePriceTextView = itemView.findViewById(R.id.vet_date);
        }
    }

    VetVisitDBHelper vetVisitDBHelper;

    public VetVisitAdapter(Context context) {
        super(context, null);
        vetVisitDBHelper = new VetVisitDBHelper(context);
        changeCursor(vetVisitDBHelper.allItems());
    }

    public void addVetVisit(String doctor, String reason, Date date){
        vetVisitDBHelper.addItem(doctor, reason, date);
        changeCursor(vetVisitDBHelper.allItems());
        notifyDataSetChanged();
    }

    /***
     * inflate the item layout and create the holder,
     */
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.row_vet_visit, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String reason = cursor.getString(cursor.getColumnIndex(VetVisitDBHelper.KEY_REASON));
        String doctor = cursor.getString(cursor.getColumnIndex(VetVisitDBHelper.KEY_DOCTOR));
        String date = cursor.getString(cursor.getColumnIndex(VetVisitDBHelper.KEY_DATE));

        viewHolder.reasonTextView.setText(reason);
        viewHolder.doctorPriceTextView.setText(doctor);
        viewHolder.datePriceTextView.setText(date);
    }



}