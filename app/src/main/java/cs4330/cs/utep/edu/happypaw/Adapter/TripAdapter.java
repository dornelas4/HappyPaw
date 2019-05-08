package cs4330.cs.utep.edu.happypaw.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cs4330.cs.utep.edu.happypaw.Activity.HomeActivity;
import cs4330.cs.utep.edu.happypaw.Activity.TripSummaryActivity;
import cs4330.cs.utep.edu.happypaw.R;
import cs4330.cs.utep.edu.happypaw.Helpers.TripDBHelper;

public class TripAdapter extends CursorRecyclerViewAdapter<TripAdapter.ViewHolder> {

    ArrayList<Integer> pos2id;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView dateTextView;
        public RelativeLayout viewBackground, viewForeground;


        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_trip);
            viewBackground = itemView.findViewById(R.id.trip_background);
            viewForeground = itemView.findViewById(R.id.trip_foreground);
        }

    }

    TripDBHelper tripDBHelper;

    public TripAdapter(Context context) {
        super(context, null);
        tripDBHelper = new TripDBHelper(context);
        pos2id = new ArrayList<Integer>();
        changeCursor(tripDBHelper.getAllTeripsCursor());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.row_trip, viewGroup, false);

        return new TripAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TripAdapter.ViewHolder viewHolder, Cursor cursor) {
        String date = cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_START_DATE));
        int id = cursor.getInt(cursor.getColumnIndex(TripDBHelper.KEY_ID));
        pos2id.add(id);

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TripSummaryActivity.class);
            intent.putExtra("tripID", id);
            ((HomeActivity)getContext()).startActivity(intent);

        });

        viewHolder.dateTextView.setText(date);
    }

    public void deleteTrip(int pos){
        tripDBHelper.deleteTrip(pos2id.get(pos), true);
        pos2id.remove(pos);
        swapCursor(tripDBHelper.getAllTeripsCursor());
        notifyDataSetChanged();
    }

}
