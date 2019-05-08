package cs4330.cs.utep.edu.happypaw.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs4330.cs.utep.edu.happypaw.Adapter.TripAdapter;
import cs4330.cs.utep.edu.happypaw.R;

import cs4330.cs.utep.edu.happypaw.Helper.TripTouchHelper;



public class TripTabFragment extends Fragment  implements TripTouchHelper.RecyclerItemTouchHelperListener {

    Context ctx;
    TripAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_trip_tab, container, false);
        ctx = getActivity();
        adapter = new TripAdapter(ctx);

        RecyclerView rcTrip = root.findViewById(R.id.recycler_trip);
        rcTrip.setLayoutManager(new LinearLayoutManager(ctx));
        rcTrip.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new TripTouchHelper(this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rcTrip);

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        adapter.deleteTrip(position);
    }
}
