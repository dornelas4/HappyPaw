package cs4330.cs.utep.edu.happypaw.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cs4330.cs.utep.edu.happypaw.R;
import cs4330.cs.utep.edu.happypaw.VetVisitDBHelper;

public class TripAdapter extends CursorRecyclerViewAdapter<TripAdapter.ViewHolder> {

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

    }

    VetVisitDBHelper vetVisitDBHelper;

    public TripAdapter(Context context) {
        super(context, null);
        vetVisitDBHelper = new VetVisitDBHelper(context);
        changeCursor(vetVisitDBHelper.allItems());
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

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
}
